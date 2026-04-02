package com.sample.travel.infrastructure.mapper

import org.mybatis.spring.boot.test.autoconfigure.MybatisTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification

@MybatisTest
@ActiveProfiles("test")
@Sql(scripts = ["classpath:sql/mapper/FavoritesMapper.sql"])
class FavoritesMapperSpec extends Specification {

    @Autowired
    FavoritesMapper mapper

    def "countByContentId — 存在するコンテンツIDで 1 が返ること"() {
        when:
        def count = mapper.countByContentId(1L)

        then:
        count == 1
    }

    def "countByContentId — 存在しないコンテンツIDで 0 が返ること"() {
        when:
        def count = mapper.countByContentId(999L)

        then:
        count == 0
    }

    def "countByUserIdAndContentId — お気に入り登録済みの場合 1 が返ること"() {
        when:
        def count = mapper.countByUserIdAndContentId(1L, 1L)

        then:
        count == 1
    }

    def "countByUserIdAndContentId — お気に入り未登録の場合 0 が返ること"() {
        when:
        def count = mapper.countByUserIdAndContentId(1L, 2L)

        then:
        count == 0
    }

    def "insert — お気に入りを登録後にカウントが 1 になること"() {
        when:
        mapper.insert(1L, 2L)

        then:
        mapper.countByUserIdAndContentId(1L, 2L) == 1
    }

    def "deleteByUserIdAndContentId — お気に入りを削除後にカウントが 0 になること"() {
        when:
        mapper.deleteByUserIdAndContentId(1L, 1L)

        then:
        mapper.countByUserIdAndContentId(1L, 1L) == 0
    }
}
