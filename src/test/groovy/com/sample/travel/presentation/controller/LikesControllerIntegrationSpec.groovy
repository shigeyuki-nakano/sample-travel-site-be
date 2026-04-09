package com.sample.travel.presentation.controller

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpMethod
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

/**
 * お気に入り登録・解除 API の統合テスト。
 *
 * シードデータ（seed.sql）前提:
 *   - content_id=1: user_id=1 のお気に入り未登録
 *   - content_id=2: user_id=1 のお気に入り登録済み
 *   - content_id=3: user_id=1 のお気に入り未登録
 *   - content_id=6: user_id=1 のお気に入り登録済み
 *   - content_id=999: 存在しないコンテンツ
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
class LikesControllerIntegrationSpec extends Specification {

    @LocalServerPort
    int port

    /** 通常のリクエスト用（201/204 などの成功レスポンスを返す） */
    RestTemplate restTemplate = new RestTemplate()

    /** エラーレスポンスのボディを取得するため、例外をスローしない RestTemplate */
    RestTemplate noThrowRestTemplate = buildNoThrowRestTemplate()

    def baseUrl() { "http://localhost:${port}" }

    def "POST /like 正常系 — HTTP 201 が返り、GET で isFavorite が true になること"() {
        when: "未登録コンテンツをお気に入り登録する"
        def postResponse = restTemplate.postForEntity("${baseUrl()}/v1/contents/1/like", null, Void)

        then:
        postResponse.statusCode.value() == 201

        when: "コンテンツ一覧を取得する"
        def getResponse = restTemplate.getForEntity("${baseUrl()}/v1/contents", Map)
        def content1 = getResponse.body.contents.find { it.id == 1 }

        then: "登録済みコンテンツの isFavorite が true になること"
        content1.isFavorite == true
    }

    def "DELETE /like 正常系 — HTTP 204 が返り、GET で isFavorite が false になること"() {
        when: "登録済みコンテンツのお気に入りを解除する"
        def deleteResponse = noThrowRestTemplate.exchange(
            "${baseUrl()}/v1/contents/6/like", HttpMethod.DELETE, null, Map)

        then:
        deleteResponse.statusCode.value() == 204

        when: "コンテンツ一覧を取得する"
        def getResponse = restTemplate.getForEntity("${baseUrl()}/v1/contents", Map)
        def content6 = getResponse.body.contents.find { it.id == 6 }

        then: "解除済みコンテンツの isFavorite が false になること"
        content6.isFavorite == false
    }

    def "POST /like — 存在しない contentId → HTTP 404 と type CONTENT_NOT_FOUND が返ること"() {
        when:
        def response = noThrowRestTemplate.postForEntity(
            "${baseUrl()}/v1/contents/999/like", null, Map)

        then:
        response.statusCode.value() == 404
        response.body.type == "CONTENT_NOT_FOUND"
    }

    def "POST /like — 重複登録 → HTTP 409 と type FAVORITE_ALREADY_EXISTS が返ること"() {
        when: "登録済みコンテンツを再登録する"
        def response = noThrowRestTemplate.postForEntity(
            "${baseUrl()}/v1/contents/2/like", null, Map)

        then:
        response.statusCode.value() == 409
        response.body.type == "FAVORITE_ALREADY_EXISTS"
    }

    def "DELETE /like — 未登録コンテンツの解除 → HTTP 404 と type FAVORITE_NOT_FOUND が返ること"() {
        when: "お気に入り未登録のコンテンツを解除しようとする"
        def response = noThrowRestTemplate.exchange(
            "${baseUrl()}/v1/contents/3/like", HttpMethod.DELETE, null, Map)

        then:
        response.statusCode.value() == 404
        response.body.type == "FAVORITE_NOT_FOUND"
    }

    private static RestTemplate buildNoThrowRestTemplate() {
        def template = new RestTemplate()
        template.errorHandler = [
            hasError   : { response -> false },
            handleError: { response -> }
        ] as ResponseErrorHandler
        return template
    }
}
