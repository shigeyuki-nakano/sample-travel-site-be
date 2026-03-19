package com.sample.travel.infrastructure.repository

import com.sample.travel.infrastructure.entity.ContentEntity
import com.sample.travel.infrastructure.entity.ContentImageEntity
import com.sample.travel.infrastructure.mapper.ContentImageMapper
import com.sample.travel.infrastructure.mapper.ContentsMapper
import spock.lang.Specification

import java.math.BigDecimal
import java.time.LocalDateTime

class ContentsRepositorySpec extends Specification {

    ContentsMapper contentsMapper = Mock()
    ContentImageMapper contentImageMapper = Mock()
    ContentsRepositoryImpl repository = new ContentsRepositoryImpl(contentsMapper, contentImageMapper)

    def "正常系：コンテンツと画像あり — ContentEntity が Content record に正しく変換され、画像が紐付けられること"() {
        given:
        def entity = new ContentEntity(
            id: 1L, title: "東京のアパート",
            prefectureName: "東京都", countryName: "日本",
            address: "新宿区", distance: "駅から徒歩5分",
            price: 15000,
            availableFrom: LocalDateTime.of(2026, 3, 20, 0, 0),
            availableTo: LocalDateTime.of(2026, 3, 25, 0, 0),
            averageRating: new BigDecimal("4.97"), reviewCount: 2L,
            favorite: false
        )
        def image1 = new ContentImageEntity(contentId: 1L, imageUrl: "https://example.com/img1.jpg", sortOrder: 1)
        def image2 = new ContentImageEntity(contentId: 1L, imageUrl: "https://example.com/img2.jpg", sortOrder: 2)
        contentsMapper.findAll() >> [entity]
        contentImageMapper.findAll() >> [image1, image2]

        when:
        def result = repository.findAll()

        then:
        result.size() == 1
        with(result[0]) {
            id == 1L
            title == "東京のアパート"
            prefectureName == "東京都"
            countryName == "日本"
            address == "新宿区"
            distance == "駅から徒歩5分"
            price == 15000
            averageRating == new BigDecimal("4.97")
            reviewCount == 2L
            !favorite
            images == ["https://example.com/img1.jpg", "https://example.com/img2.jpg"]
        }
    }

    def "画像なし — images が空リストであること"() {
        given:
        def entity = new ContentEntity(id: 1L, title: "テスト宿", prefectureName: "東京都",
            countryName: "日本", address: "", distance: "", price: 10000,
            availableFrom: LocalDateTime.now(), availableTo: LocalDateTime.now(),
            averageRating: null, reviewCount: 0L, favorite: false)
        contentsMapper.findAll() >> [entity]
        contentImageMapper.findAll() >> []

        when:
        def result = repository.findAll()

        then:
        result[0].images == []
    }

    def "複数コンテンツ・画像が混在 — 各コンテンツに対応する画像のみが紐付けられること"() {
        given:
        def entity1 = new ContentEntity(id: 1L, title: "宿A", prefectureName: "東京都",
            countryName: "日本", address: "", distance: "", price: 10000,
            availableFrom: LocalDateTime.now(), availableTo: LocalDateTime.now(),
            averageRating: null, reviewCount: 0L, favorite: false)
        def entity2 = new ContentEntity(id: 2L, title: "宿B", prefectureName: "沖縄県",
            countryName: "日本", address: "", distance: "", price: 20000,
            availableFrom: LocalDateTime.now(), availableTo: LocalDateTime.now(),
            averageRating: null, reviewCount: 0L, favorite: true)
        def imageA = new ContentImageEntity(contentId: 1L, imageUrl: "https://example.com/a.jpg", sortOrder: 1)
        def imageB1 = new ContentImageEntity(contentId: 2L, imageUrl: "https://example.com/b1.jpg", sortOrder: 1)
        def imageB2 = new ContentImageEntity(contentId: 2L, imageUrl: "https://example.com/b2.jpg", sortOrder: 2)
        contentsMapper.findAll() >> [entity1, entity2]
        contentImageMapper.findAll() >> [imageA, imageB1, imageB2]

        when:
        def result = repository.findAll()

        then:
        result[0].images == ["https://example.com/a.jpg"]
        result[1].images == ["https://example.com/b1.jpg", "https://example.com/b2.jpg"]
    }

    def "データ0件 — 空の List<Content> が返ること"() {
        given:
        contentsMapper.findAll() >> []
        contentImageMapper.findAll() >> []

        when:
        def result = repository.findAll()

        then:
        result == []
    }
}
