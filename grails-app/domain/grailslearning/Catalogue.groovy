package grailslearning

class Catalogue {

    String id
    String name

    static constraints = {
        id generator: 'uuid', lenght: 36
        name blank: false, maxSize: 255
    }
}
