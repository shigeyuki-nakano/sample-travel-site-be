package com.sample.travel.infrastructure.mapper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.math.BigDecimal

@SpringBootTest
class ContentsMapperSpec extends Specification {

    @Autowired
    ContentsMapper mapper

    def "findAll() がテストデータの全件（3件）を返すこと"() {
        when:
        def result = mapper.findAll()

        then:
        result.size() == 3
    }

    def "findAll() が id 昇順で返すこと"() {
        when:
        def result = mapper.findAll()

        then:
        result[0].id == 1L
        result[2].id == 3L
    }

    def "content_id=1 の averageRating と reviewCount がテストデータと一致すること"() {
        when:
        def result = mapper.findAll()
        def content1 = result.find { it.id == 1L }

        then:
        content1 != null
        content1.reviewCount == 2L
        content1.averageRating.compareTo(new BigDecimal("4.97")) == 0
    }

    def "user_id=1 がお気に入り登録済みの content_id=2 で favorite=true になること"() {
        when:
        def result = mapper.findAll()
        def content2 = result.find { it.id == 2L }

        then:
        content2 != null
        content2.favorite == true
    }

    def "お気に入り未登録の content_id=1 で favorite=false になること"() {
        when:
        def result = mapper.findAll()
        def content1 = result.find { it.id == 1L }

        then:
        content1 != null
        content1.favorite == false
    }
}
