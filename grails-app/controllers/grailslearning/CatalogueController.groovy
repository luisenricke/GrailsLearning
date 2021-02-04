package grailslearning

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class CatalogueController {

    CatalogueService catalogueService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond catalogueService.list(params), model:[catalogueCount: catalogueService.count()]
    }

    def show(String id) {
        respond catalogueService.get(id)
    }

    def create() {
        respond new Catalogue(params)
    }

    def save(Catalogue catalogue) {
        if (catalogue == null) {
            notFound()
            return
        }

        try {
            catalogueService.save(catalogue)
        } catch (ValidationException e) {
            respond catalogue.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'catalogue.label', default: 'Catalogue'), catalogue.id])
                redirect catalogue
            }
            '*' { respond catalogue, [status: CREATED] }
        }
    }

    def edit(String id) {
        respond catalogueService.get(id)
    }

    def update(Catalogue catalogue) {
        if (catalogue == null) {
            notFound()
            return
        }

        try {
            catalogueService.save(catalogue)
        } catch (ValidationException e) {
            respond catalogue.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'catalogue.label', default: 'Catalogue'), catalogue.id])
                redirect catalogue
            }
            '*'{ respond catalogue, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        catalogueService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'catalogue.label', default: 'Catalogue'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'catalogue.label', default: 'Catalogue'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
