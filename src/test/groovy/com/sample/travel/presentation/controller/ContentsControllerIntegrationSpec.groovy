package com.sample.travel.presentation.controller

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
class ContentsControllerIntegrationSpec extends Specification {

    @LocalServerPort
    int port

    RestTemplate restTemplate = new RestTemplate()

    def baseUrl() { "http://localhost:${port}" }

    def "GET /v1/contents が HTTP 200 を返すこと"() {
        when:
        def response = restTemplate.getForEntity("${baseUrl()}/v1/contents", Map)

        then:
        response.statusCode.value() == 200
    }

    def "contents の件数がシードデータ（8件）と一致すること"() {
        when:
        def response = restTemplate.getForEntity("${baseUrl()}/v1/contents", Map)

        then:
        response.body.contents.size() == 8
    }

    def "contents[0] に必須フィールドが全て存在すること"() {
        when:
        def response = restTemplate.getForEntity("${baseUrl()}/v1/contents", Map)
        def first = response.body.contents[0]

        then:
        first.id != null
        first.title != null
        first.location != null
        first.dates != null
        first.images != null
    }

    def "content_id=2 のコンテンツで isFavorite=true であること"() {
        when:
        def response = restTemplate.getForEntity("${baseUrl()}/v1/contents", Map)
        def content2 = response.body.contents.find { it.id == 2 }

        then:
        content2.isFavorite == true
    }

    def "各コンテンツの images が空でないこと"() {
        when:
        def response = restTemplate.getForEntity("${baseUrl()}/v1/contents", Map)

        then:
        response.body.contents.every { it.images != null && !(it.images as List).isEmpty() }
    }
}
