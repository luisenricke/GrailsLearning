package grailslearning

class BootStrap {

    def init = { servletContext ->
        log.debug "This is a debug message"
        log.info "This is an info message"
        log.warn "This is a warn message"
        log.error "This is an error message"
    }
    def destroy = {
    }
}
