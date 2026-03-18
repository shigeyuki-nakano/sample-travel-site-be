package com.sample.travel.infrastructure.entity

import spock.lang.Specification

import java.math.BigDecimal
import java.time.LocalDateTime

class ContentEntitySpec extends Specification {

    def "セッターで全フィールドをセットしてゲッターで取得できること"() {
        given:
        def now = LocalDateTime.now()
        def entity = new ContentEntity()

        when:
        entity.id = 1L
        entity.title = "海を一望できる絶景ヴィラ"
        entity.prefectureName = "沖縄県"
        entity.countryName = "日本"
        entity.address = "那覇市"
        entity.distance = "海岸から徒歩2分"
        entity.price = 28000
        entity.availableFrom = now
        entity.availableTo = now.plusDays(5)
        entity.averageRating = new BigDecimal("4.97")
        entity.reviewCount = 3L
        entity.favorite = true

        then:
        entity.id == 1L
        entity.title == "海を一望できる絶景ヴィラ"
        entity.prefectureName == "沖縄県"
        entity.countryName == "日本"
        entity.address == "那覇市"
        entity.distance == "海岸から徒歩2分"
        entity.price == 28000
        entity.availableFrom == now
        entity.availableTo == now.plusDays(5)
        entity.averageRating == new BigDecimal("4.97")
        entity.reviewCount == 3L
        entity.favorite
    }

    def "averageRating が null のインスタンスを生成できること"() {
        given:
        def entity = new ContentEntity()

        when:
        entity.averageRating = null
        entity.reviewCount = 0L

        then:
        entity.averageRating == null
        entity.reviewCount == 0L
    }

    def "同じフィールドを持つ 2 つのインスタンスは等しいこと"() {
        given:
        def now = LocalDateTime.now()
        def a = new ContentEntity()
        def b = new ContentEntity()
        [a, b].each {
            it.id = 1L
            it.title = "タイトル"
            it.prefectureName = "東京都"
            it.countryName = "日本"
            it.address = ""
            it.distance = ""
            it.price = 1000
            it.availableFrom = now
            it.availableTo = now.plusDays(1)
            it.averageRating = null
            it.reviewCount = 0L
            it.favorite = false
        }

        expect:
        a == b
    }
}
