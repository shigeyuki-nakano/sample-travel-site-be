package com.sample.travel.presentation.controller

import com.sample.travel.domain.model.Content
import com.sample.travel.domain.service.ContentsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import java.math.BigDecimal
import java.time.LocalDateTime

import static org.hamcrest.Matchers.containsString
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(ContentsController)
class ContentsControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @MockitoBean
    ContentsService contentsService

    def "正常系 — HTTP 200 と contents[0].id が返ること"() {
        given:
        def content = content(1L, "東京のアパート", "東京都", "日本",
            new BigDecimal("4.97"), 2L, false,
            LocalDateTime.of(2026, 3, 20, 0, 0), LocalDateTime.of(2026, 3, 25, 0, 0),
            ["https://example.com/img1.jpg"])
        when(contentsService.findAll()).thenReturn([content])

        expect:
        mockMvc.perform(get("/v1/contents"))
            .andExpect(status().isOk())
            .andExpect(jsonPath('$.contents[0].id').value(1))
    }

    def "データ0件 — HTTP 200 と空配列が返ること"() {
        given:
        when(contentsService.findAll()).thenReturn([])

        expect:
        mockMvc.perform(get("/v1/contents"))
            .andExpect(status().isOk())
            .andExpect(jsonPath('$.contents').isArray())
            .andExpect(jsonPath('$.contents').isEmpty())
    }

    def "DTO変換 — location が都道府県名と国名で組み立てられること"() {
        given:
        def content = content(1L, "沖縄のヴィラ", "沖縄県", "日本",
            new BigDecimal("4.97"), 2L, false,
            LocalDateTime.of(2026, 3, 20, 0, 0), LocalDateTime.of(2026, 3, 25, 0, 0),
            [])
        when(contentsService.findAll()).thenReturn([content])

        expect:
        mockMvc.perform(get("/v1/contents"))
            .andExpect(status().isOk())
            .andExpect(jsonPath('$.contents[0].location').value("沖縄県、日本"))
    }

    def "DTO変換 — dates が M月D日〜D日 形式でフォーマットされること"() {
        given:
        def content = content(1L, "テスト宿", "東京都", "日本",
            new BigDecimal("4.97"), 2L, false,
            LocalDateTime.of(2026, 3, 20, 0, 0), LocalDateTime.of(2026, 3, 25, 0, 0),
            [])
        when(contentsService.findAll()).thenReturn([content])

        expect:
        mockMvc.perform(get("/v1/contents"))
            .andExpect(status().isOk())
            .andExpect(jsonPath('$.contents[0].dates').value("3月20日〜25日"))
    }

    def "DTO変換 — averageRating >= 4.9 かつ reviewCount >= 1 のとき isGuestFavorite が true になること"() {
        given:
        def content = content(1L, "テスト宿", "東京都", "日本",
            new BigDecimal("4.90"), 1L, false,
            LocalDateTime.of(2026, 3, 20, 0, 0), LocalDateTime.of(2026, 3, 25, 0, 0),
            [])
        when(contentsService.findAll()).thenReturn([content])

        expect:
        mockMvc.perform(get("/v1/contents"))
            .andExpect(status().isOk())
            .andExpect(jsonPath('$.contents[0].isGuestFavorite').value(true))
    }

    def "DTO変換 — averageRating が null のとき rating が null になること"() {
        given:
        def content = content(1L, "テスト宿", "東京都", "日本",
            null, 0L, false,
            LocalDateTime.of(2026, 3, 20, 0, 0), LocalDateTime.of(2026, 3, 25, 0, 0),
            [])
        when(contentsService.findAll()).thenReturn([content])

        expect:
        mockMvc.perform(get("/v1/contents"))
            .andExpect(status().isOk())
            .andExpect(jsonPath('$.contents[0].rating').doesNotExist())
    }

    def "Service が例外をスロー — HTTP 500 と message・type フィールドが返ること"() {
        given:
        when(contentsService.findAll()).thenThrow(new RuntimeException("DB error"))

        expect:
        mockMvc.perform(get("/v1/contents"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath('$.message').exists())
            .andExpect(jsonPath('$.type').value("INTERNAL_SERVER_ERROR"))
    }

    def "CORS — 許可オリジンからのリクエストで Access-Control-Allow-Origin ヘッダーが返却されること"() {
        given:
        when(contentsService.findAll()).thenReturn([])

        expect:
        mockMvc.perform(get("/v1/contents").header("Origin", "http://localhost:3000"))
            .andExpect(status().isOk())
            .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
    }

    def "CORS — プリフライトリクエストが HTTP 200 で応答し許可メソッド・ヘッダーが返却されること"() {
        expect:
        mockMvc.perform(options("/v1/contents")
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "POST")
                .header("Access-Control-Request-Headers", "Content-Type,Authorization"))
            .andExpect(status().isOk())
            .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
            .andExpect(header().string("Access-Control-Allow-Methods", containsString("POST")))
            .andExpect(header().string("Access-Control-Allow-Headers", containsString("Content-Type")))
            .andExpect(header().string("Access-Control-Allow-Headers", containsString("Authorization")))
    }

    def "CORS — 非許可オリジンからのリクエストで Access-Control-Allow-Origin ヘッダーが返却されないこと"() {
        given:
        when(contentsService.findAll()).thenReturn([])

        expect:
        mockMvc.perform(get("/v1/contents").header("Origin", "http://localhost:9999"))
            .andExpect(status().isForbidden())
            .andExpect(header().doesNotExist("Access-Control-Allow-Origin"))
    }

    private static Content content(Long id, String title, String prefecture, String country,
                                   BigDecimal rating, Long reviewCount, boolean favorite,
                                   LocalDateTime from, LocalDateTime to, List<String> images) {
        new Content(id, title, prefecture, country, "", "駅から徒歩5分", 10000,
            from, to, rating, reviewCount, favorite, images)
    }
}
