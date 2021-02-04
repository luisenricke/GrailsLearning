package grailslearning

import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import spock.lang.*

// https://guides.grails.org/grails3/grails-controller-testing/guide/index.html
// https://guides.grails.org/grails3/grails-custom-security-tenant-resolver/guide/index.html
// http://spockframework.org/spock/docs/1.3/all_in_one.html
// https://medium.com/@xala3pa/mocking-with-spock-51c8e2fb6cb6
class CatalogueControllerSpec extends Specification implements ControllerUnitTest<CatalogueController>, DomainUnitTest<Catalogue> {

    def populateValidParams(params) {
        assert params != null
        params["name"] = 'Luis Villalobos'
    }

    def "Test the index action returns the correct model"() {
        given:
        controller.catalogueService = Mock(CatalogueService) {
            1 * list(_) >> []
            1 * count() >> 0
        }

        when:"The index action is executed"
        controller.index()

        then:"The model is correct"
        !model.catalogueList
        model.catalogueCount == 0
    }

    def "Test the create action returns the correct model"() {
        when:"The create action is executed"
        controller.create()

        then:"The model is correctly created"
        model.catalogue!= null
    }

    def "Test the save action with a null instance"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save(null)

        then:"A 404 error is returned"
        response.redirectedUrl == '/catalogue/index'
        flash.message != null
    }

    def "Test the save action correctly persists"() {
        given:
        controller.catalogueService = Mock(CatalogueService) {
            1 * save(_ as Catalogue)
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        def catalogue = new Catalogue(params)
        catalogue.id = '1'

        controller.save(catalogue)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/catalogue/show/1'
        controller.flash.message != null
    }

    def "Test the save action with an invalid instance"() {
        given:
        controller.catalogueService = Mock(CatalogueService) {
            1 * save(_ as Catalogue) >> { Catalogue catalogue ->
                throw new ValidationException("Invalid instance", catalogue.errors)
            }
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        def catalogue = new Catalogue()
        controller.save(catalogue)

        then:"The create view is rendered again with the correct model"
        model.catalogue != null
        view == 'create'
    }

    def "Test the show action with a null id"() {
        given:
        controller.catalogueService = Mock(CatalogueService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.show(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    def "Test the show action with a valid id"() {
        given:
        controller.catalogueService = Mock(CatalogueService) {
            1 * get('2') >> new Catalogue()
        }

        when:"A domain instance is passed to the show action"
        controller.show('2')

        then:"A model is populated containing the domain instance"
        model.catalogue instanceof Catalogue
    }

    def "Test the edit action with a null id"() {
        given:
        controller.catalogueService = Mock(CatalogueService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.edit(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    def "Test the edit action with a valid id"() {
        given:
        controller.catalogueService = Mock(CatalogueService) {
            1 * get('2') >> new Catalogue()
        }

        when:"A domain instance is passed to the show action"
        controller.edit('2')

        then:"A model is populated containing the domain instance"
        model.catalogue instanceof Catalogue
    }

    def "Test the update action with a null instance"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(null)

        then:"A 404 error is returned"
        response.redirectedUrl == '/catalogue/index'
        flash.message != null
    }

    def "Test the update action correctly persists"() {
        given:
        controller.catalogueService = Mock(CatalogueService) {
            1 * save(_ as Catalogue)
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        populateValidParams(params)
        def catalogue = new Catalogue(params)
        catalogue.id = 1

        controller.update(catalogue)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/catalogue/show/1'
        controller.flash.message != null
    }

    def "Test the update action with an invalid instance"() {
        given:
        controller.catalogueService = Mock(CatalogueService) {
            1 * save(_ as Catalogue) >> { Catalogue catalogue ->
                throw new ValidationException("Invalid instance", catalogue.errors)
            }
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(new Catalogue())

        then:"The edit view is rendered again with the correct model"
        model.catalogue != null
        view == 'edit'
    }

    def "Test the delete action with a null instance"() {
        when:"The delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(null)

        then:"A 404 is returned"
        response.redirectedUrl == '/catalogue/index'
        flash.message != null
    }

    def "Test the delete action with an instance"() {
        given:
        controller.catalogueService = Mock(CatalogueService) {
            1 * delete(2)
        }

        when:"The domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2)

        then:"The user is redirected to index"
        response.redirectedUrl == '/catalogue/index'
        flash.message != null
    }
}






