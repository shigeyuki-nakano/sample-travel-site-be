package com.sample.travel.infrastructure.repository

import com.sample.travel.domain.exception.ContentNotFoundException
import com.sample.travel.domain.exception.FavoriteAlreadyExistsException
import com.sample.travel.domain.exception.FavoriteNotFoundException
import com.sample.travel.infrastructure.mapper.FavoritesMapper
import spock.lang.Specification

class FavoritesRepositoryImplSpec extends Specification {

    FavoritesMapper favoritesMapper = Mock()
    FavoritesRepositoryImpl repository = new FavoritesRepositoryImpl(favoritesMapper)

    // ===== add =====

    def "add — 正常系: コンテンツ存在・未登録のとき insert が呼ばれること"() {
        given:
        favoritesMapper.countByContentId(10L) >> 1
        favoritesMapper.countByUserIdAndContentId(1L, 10L) >> 0

        when:
        repository.add(1L, 10L)

        then:
        1 * favoritesMapper.insert(1L, 10L)
    }

    def "add — contentId が存在しないとき ContentNotFoundException がスローされること"() {
        given:
        favoritesMapper.countByContentId(99L) >> 0

        when:
        repository.add(1L, 99L)

        then:
        thrown(ContentNotFoundException)
        0 * favoritesMapper.insert(*_)
    }

    def "add — 既にお気に入り登録済みのとき FavoriteAlreadyExistsException がスローされること"() {
        given:
        favoritesMapper.countByContentId(10L) >> 1
        favoritesMapper.countByUserIdAndContentId(1L, 10L) >> 1

        when:
        repository.add(1L, 10L)

        then:
        thrown(FavoriteAlreadyExistsException)
        0 * favoritesMapper.insert(*_)
    }

    def "add — コンテンツ存在確認を先に行い、重複確認は後に行うこと"() {
        when:
        repository.add(1L, 10L)

        then:
        1 * favoritesMapper.countByContentId(10L) >> 1

        then:
        1 * favoritesMapper.countByUserIdAndContentId(1L, 10L) >> 0

        then:
        1 * favoritesMapper.insert(1L, 10L)
    }

    // ===== remove =====

    def "remove — 正常系: コンテンツ存在・登録済みのとき deleteByUserIdAndContentId が呼ばれること"() {
        given:
        favoritesMapper.countByContentId(10L) >> 1
        favoritesMapper.countByUserIdAndContentId(1L, 10L) >> 1

        when:
        repository.remove(1L, 10L)

        then:
        1 * favoritesMapper.deleteByUserIdAndContentId(1L, 10L)
    }

    def "remove — contentId が存在しないとき ContentNotFoundException がスローされること"() {
        given:
        favoritesMapper.countByContentId(99L) >> 0

        when:
        repository.remove(1L, 99L)

        then:
        thrown(ContentNotFoundException)
        0 * favoritesMapper.deleteByUserIdAndContentId(*_)
    }

    def "remove — お気に入り未登録のとき FavoriteNotFoundException がスローされること"() {
        given:
        favoritesMapper.countByContentId(10L) >> 1
        favoritesMapper.countByUserIdAndContentId(1L, 10L) >> 0

        when:
        repository.remove(1L, 10L)

        then:
        thrown(FavoriteNotFoundException)
        0 * favoritesMapper.deleteByUserIdAndContentId(*_)
    }
}
