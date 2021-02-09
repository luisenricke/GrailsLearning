package grailslearning

class Catalogue {

    String id
    String name

    static constraints = {
        id generator: 'uuid'
        name blank: false, maxSize: 255
    }

}
