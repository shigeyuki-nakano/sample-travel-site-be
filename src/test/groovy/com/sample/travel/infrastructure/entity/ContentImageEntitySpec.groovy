package com.sample.travel.infrastructure.entity

import spock.lang.Specification

class ContentImageEntitySpec extends Specification {

    def "セッターで全フィールドをセットしてゲッターで取得できること"() {
        given:
        def entity = new ContentImageEntity()

        when:
        entity.contentId = 1L
        entity.imageUrl = "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=800&q=80"
        entity.sortOrder = 1

        then:
        entity.contentId == 1L
        entity.imageUrl == "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=800&q=80"
        entity.sortOrder == 1
    }

    def "同じフィールドを持つ 2 つのインスタンスは等しいこと"() {
        given:
        def a = new ContentImageEntity()
        def b = new ContentImageEntity()
        [a, b].each {
            it.contentId = 2L
            it.imageUrl = "https://example.com/img.jpg"
            it.sortOrder = 0
        }

        expect:
        a == b
    }
}
