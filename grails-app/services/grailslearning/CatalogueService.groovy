package grailslearning

import grails.gorm.services.Service

// https://guides.grails.org/grails-gorm-data-services/guide/index.html

interface ICatalogueService {

    Catalogue get(Serializable id)

    List<Catalogue> list(Map args)

    Long count()

    void delete(Serializable id)

    Catalogue save(Catalogue catalogue)

    List<Catalogue> findByName(String name)

}

@Service(Catalogue)
abstract class CatalogueService implements ICatalogueService{

}
