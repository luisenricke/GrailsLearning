package grailslearning

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

// https://guides.grails.org/grails-test-domain-class-constraints/guide/index.html
// https://github.com/grails-guides/grails-test-domain-class-constraints
// http://spockframework.org/spock/docs/1.3/all_in_one.html
// https://github.com/spockframework/spock-example
class CatalogueSpec extends Specification implements DomainUnitTest<Catalogue> {

    @Subject
    Catalogue catalogue

    def setup() {
        catalogue = new Catalogue()
    }

    def cleanup() {
        catalogue = null // Garbage collector
    }

    def "test name cannot be null"() {
        when: 'set the name'
        catalogue.name = null

        then: 'check the validation of null'
        !catalogue.validate(['name'])
        catalogue.errors['name'].code == 'nullable'
    }

    def "test name cannot be blank"() {
        when: 'set the name'
        catalogue.name = ''

        then: 'check the validation of null'
        !catalogue.validate(['name'])
        catalogue.errors['name'].code == 'blank'
    }

    def "test name can have a maximum of 255 characters"() {
        String aux = ''
        when: 'for a string of 256 characters'
        aux = 'a' * 256
        catalogue.name = aux

        then: 'validation of string size fails'
        !catalogue.validate(['name'])
        catalogue.errors['name'].code == 'maxSize.exceeded'

        when: 'for a string of 255 characters'
        aux = 'a' * 255
        catalogue.name = aux

        then: 'validation of string size pass'
        catalogue.validate(['name'])
    }

    @Unroll('Is the same value in Catalogue.name with #value and #expected ? It is expects #expectedErrorCode')
    def "test list of names valid"() {
        def names = ['dog', 'cat']
        when: 'put the value of the table'
        catalogue.name = value

        then: 'validate the name'
        names.contains(expected) == expectedErrorCode

        where: 'table of values'
        value   | expected | expectedErrorCode
        'dog'   | 'dog'    | true
        'cat'   | 'cat'    | true
        'bird'  | 'test'   | false
        'other' | 'fail'   | false
    }
}
