package com.sample.travel.domain.service

import com.sample.travel.domain.model.Content
import com.sample.travel.domain.repository.ContentsRepository
import spock.lang.Specification

import java.time.LocalDateTime

class ContentsServiceSpec extends Specification {

    ContentsRepository contentsRepository = Mock()
    ContentsService service = new ContentsService(contentsRepository)

    def "正常系：データあり — ContentsRepository#findAll() の戻り値がそのまま返ること"() {
        given:
        def content = new Content(
                1L, "東京のアパート", "東京都", "日本",
                "新宿区", "駅から徒歩5分", 15000,
                LocalDateTime.of(2026, 3, 20, 0, 0),
                LocalDateTime.of(2026, 3, 25, 0, 0),
                new BigDecimal("4.97"), 2L, false,
                ["https://example.com/img1.jpg"]
        )
        contentsRepository.findAll() >> [content]

        when:
        def result = service.findAll()

        then:
        result == [content]
    }

    def "正常系：データ0件 — 空リストが返ること"() {
        given:
        contentsRepository.findAll() >> []

        when:
        def result = service.findAll()

        then:
        result == []
    }
}
