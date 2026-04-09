package com.sample.travel.domain.exception

import spock.lang.Specification

class DomainExceptionsSpec extends Specification {

    def "ContentNotFoundException — contentId がメッセージに含まれること"() {
        when:
        def ex = new ContentNotFoundException(99L)

        then:
        ex instanceof RuntimeException
        ex.message.contains("99")
    }

    def "FavoriteNotFoundException — contentId がメッセージに含まれること"() {
        when:
        def ex = new FavoriteNotFoundException(99L)

        then:
        ex instanceof RuntimeException
        ex.message.contains("99")
    }

    def "FavoriteAlreadyExistsException — contentId がメッセージに含まれること"() {
        when:
        def ex = new FavoriteAlreadyExistsException(1L)

        then:
        ex instanceof RuntimeException
        ex.message.contains("1")
    }
}
