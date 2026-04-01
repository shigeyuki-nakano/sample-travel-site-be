package com.sample.travel.infrastructure.mapper

import org.mybatis.spring.boot.test.autoconfigure.MybatisTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification

@MybatisTest
@ActiveProfiles("test")
@Sql(scripts = ["classpath:sql/mapper/ContentImageMapper.sql"])
class ContentImageMapperSpec extends Specification {

    @Autowired
    ContentImageMapper mapper

    def "findAll() がテストデータの全件（3件）を返すこと"() {
        when:
        def result = mapper.findAll()

        then:
        result.size() == 3
    }

    def "findAll() が content_id 昇順・sort_order 昇順で返すこと"() {
        when:
        def result = mapper.findAll()

        then:
        result[0].contentId == 1L
        result[0].sortOrder == 1
        result[1].contentId == 1L
        result[1].sortOrder == 2
        result[2].contentId == 2L
        result[2].sortOrder == 1
    }

    def "content_id=1 の imageUrl がテストデータと一致すること"() {
        when:
        def result = mapper.findAll()
        def images = result.findAll { it.contentId == 1L }

        then:
        images[0].imageUrl == 'https://example.com/img1a.jpg'
        images[1].imageUrl == 'https://example.com/img1b.jpg'
    }
}
