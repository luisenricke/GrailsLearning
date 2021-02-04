package grailslearning

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification
import org.hibernate.SessionFactory

// https://guides.grails.org/grails-mock-basics/guide/index.html
// http://guides.grails.org/grails-gorm-data-services/guide/index.html
// https://guides.grails.org/grails3/grails-logicaldelete/guide/index.html
// http://spockframework.org/spock/docs/1.3/all_in_one.html
@Integration
@Rollback
class CatalogueServiceSpec extends Specification {

    @Shared CatalogueService catalogueService
    @Shared SessionFactory sessionFactory

    static private String setupData() {
        new Catalogue(name: '1').save(flush: true, failOnError: true)
        new Catalogue(name: '2').save(flush: true, failOnError: true)
        Catalogue catalogue = new Catalogue(name: '3').save(flush: true, failOnError: true)
        new Catalogue(name: '4').save(flush: true, failOnError: true)
        new Catalogue(name: '5').save(flush: true, failOnError: true)
        catalogue.name
    }

    @Ignore
    def "test connection"() {
        expect: 'Connection of session'
        sessionFactory.currentSession.connected
    }

    def "test get"() {
        setupData()

        expect:
        catalogueService.findByName('1') != null
    }

    def "test list"() {
        setupData()

        when:
        List<Catalogue> catalogueList = catalogueService.list(max: 2, offset: 2)

        then:
        catalogueList.size() == 2
    }

    def "test count"() {
        setupData()

        expect:
        catalogueService.count() == 5
    }

    def "test delete"() {
        String catalogueName = setupData()
        Catalogue catalogue = catalogueService.findByName(catalogueName)[0]

        expect:
        catalogueService.count() == 5

        when:
        catalogueService.delete(catalogue.id)
        sessionFactory.currentSession.flush()

        then:
        catalogueService.count() == 4
    }

    def "test save"() {
        when:
        def saved = catalogueService.save(new Catalogue(name: 'test'))

        then:
        saved.id != null
    }
}
