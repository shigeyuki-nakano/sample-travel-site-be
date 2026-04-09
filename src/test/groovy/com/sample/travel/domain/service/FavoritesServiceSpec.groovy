package com.sample.travel.domain.service

import com.sample.travel.domain.exception.ContentNotFoundException
import com.sample.travel.domain.exception.FavoriteAlreadyExistsException
import com.sample.travel.domain.exception.FavoriteNotFoundException
import com.sample.travel.domain.repository.FavoritesRepository
import spock.lang.Specification

class FavoritesServiceSpec extends Specification {

    FavoritesRepository favoritesRepository = Mock()
    FavoritesService service = new FavoritesService(favoritesRepository)

    def "add — Repository#add へ正しく委譲されること"() {
        when:
        service.add(1L, 10L)

        then:
        1 * favoritesRepository.add(1L, 10L)
    }

    def "remove — Repository#remove へ正しく委譲されること"() {
        when:
        service.remove(1L, 10L)

        then:
        1 * favoritesRepository.remove(1L, 10L)
    }

    def "add — Repository が ContentNotFoundException をスローした場合、そのままスローされること"() {
        given:
        favoritesRepository.add(1L, 99L) >> { throw new ContentNotFoundException(99L) }

        when:
        service.add(1L, 99L)

        then:
        thrown(ContentNotFoundException)
    }

    def "add — Repository が FavoriteAlreadyExistsException をスローした場合、そのままスローされること"() {
        given:
        favoritesRepository.add(1L, 1L) >> { throw new FavoriteAlreadyExistsException(1L) }

        when:
        service.add(1L, 1L)

        then:
        thrown(FavoriteAlreadyExistsException)
    }

    def "remove — Repository が FavoriteNotFoundException をスローした場合、そのままスローされること"() {
        given:
        favoritesRepository.remove(1L, 99L) >> { throw new FavoriteNotFoundException(99L) }

        when:
        service.remove(1L, 99L)

        then:
        thrown(FavoriteNotFoundException)
    }
}
