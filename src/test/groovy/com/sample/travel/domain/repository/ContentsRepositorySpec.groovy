package com.sample.travel.domain.repository

import com.sample.travel.domain.model.Content
import spock.lang.Specification

import java.math.BigDecimal
import java.time.LocalDateTime

class ContentsRepositorySpec extends Specification {

    def "ContentsRepository の findAll() は List<Content> を返すこと"() {
        given:
        def content = new Content(
                1L, "タイトル", "東京都", "日本", "", "駅から徒歩1分",
                10000, LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                new BigDecimal("4.50"), 2L, false, ["https://example.com/img.jpg"]
        )
        def repository = new ContentsRepository() {
            @Override
            List<Content> findAll() {
                return [content]
            }
        }

        when:
        def result = repository.findAll()

        then:
        result.size() == 1
        result[0].id() == 1L
        result[0].title() == "タイトル"
    }

    def "ContentsRepository の findAll() はデータが 0 件のとき空リストを返すこと"() {
        given:
        def repository = new ContentsRepository() {
            @Override
            List<Content> findAll() {
                return []
            }
        }

        when:
        def result = repository.findAll()

        then:
        result.isEmpty()
    }
}
