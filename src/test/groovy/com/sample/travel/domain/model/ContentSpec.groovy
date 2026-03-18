package com.sample.travel.domain.model

import spock.lang.Specification

import java.math.BigDecimal
import java.time.LocalDateTime

class ContentSpec extends Specification {

    def "全フィールドを指定してインスタンスを生成できること"() {
        given:
        def now = LocalDateTime.now()
        def images = ["https://example.com/image1.jpg", "https://example.com/image2.jpg"]

        when:
        def content = new Content(
                1L,
                "海を一望できる絶景ヴィラ",
                "沖縄県",
                "日本",
                "那覇市",
                "海岸から徒歩2分",
                28000,
                now,
                now.plusDays(5),
                new BigDecimal("4.97"),
                3L,
                false,
                images
        )

        then:
        content.id() == 1L
        content.title() == "海を一望できる絶景ヴィラ"
        content.prefectureName() == "沖縄県"
        content.countryName() == "日本"
        content.address() == "那覇市"
        content.distance() == "海岸から徒歩2分"
        content.price() == 28000
        content.availableFrom() == now
        content.availableTo() == now.plusDays(5)
        content.averageRating() == new BigDecimal("4.97")
        content.reviewCount() == 3L
        content.favorite()
        content.images() == images
    }

    def "averageRating が null のインスタンスを生成できること"() {
        when:
        def content = new Content(
                1L, "タイトル", "東京都", "日本", "", "駅から徒歩1分",
                10000, LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                null, 0L, false, []
        )

        then:
        content.averageRating() == null
        content.reviewCount() == 0L
        content.images() == []
    }

    def "同じフィールドを持つ 2 つのインスタンスは等しいこと"() {
        given:
        def now = LocalDateTime.now()
        def a = new Content(1L, "A", "東京都", "日本", "", "", 1000, now, now, null, 0L, false, [])
        def b = new Content(1L, "A", "東京都", "日本", "", "", 1000, now, now, null, 0L, false, [])

        expect:
        a == b
    }
}
