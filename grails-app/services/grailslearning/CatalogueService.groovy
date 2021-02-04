package grailslearning

import grails.gorm.services.Service

@Service(Catalogue)
interface CatalogueService {

    Catalogue get(Serializable id)

    List<Catalogue> list(Map args)

    Long count()

    void delete(Serializable id)

    Catalogue save(Catalogue catalogue)

}