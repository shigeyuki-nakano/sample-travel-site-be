package com.sample.travel.presentation.controller

import com.sample.travel.domain.exception.ContentNotFoundException
import com.sample.travel.domain.exception.FavoriteAlreadyExistsException
import com.sample.travel.domain.exception.FavoriteNotFoundException
import com.sample.travel.domain.service.FavoritesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.anyLong
import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.doThrow
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(LikesController)
class LikesControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @MockitoBean
    FavoritesService favoritesService

    def "postLike 正常系 — HTTP 201 が返ること"() {
        given:
        doNothing().when(favoritesService).add(anyLong(), anyLong())

        expect:
        mockMvc.perform(post("/v1/contents/1/like"))
            .andExpect(status().isCreated())
    }

    def "deleteLike 正常系 — HTTP 204 が返ること"() {
        given:
        doNothing().when(favoritesService).remove(anyLong(), anyLong())

        expect:
        mockMvc.perform(delete("/v1/contents/1/like"))
            .andExpect(status().isNoContent())
    }

    def "postLike — ContentNotFoundException → HTTP 404 と type CONTENT_NOT_FOUND が返ること"() {
        given:
        doThrow(new ContentNotFoundException(99L)).when(favoritesService).add(anyLong(), anyLong())

        expect:
        mockMvc.perform(post("/v1/contents/99/like"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath('$.type').value("CONTENT_NOT_FOUND"))
    }

    def "postLike — FavoriteAlreadyExistsException → HTTP 409 と type FAVORITE_ALREADY_EXISTS が返ること"() {
        given:
        doThrow(new FavoriteAlreadyExistsException(1L)).when(favoritesService).add(anyLong(), anyLong())

        expect:
        mockMvc.perform(post("/v1/contents/1/like"))
            .andExpect(status().isConflict())
            .andExpect(jsonPath('$.type').value("FAVORITE_ALREADY_EXISTS"))
    }

    def "deleteLike — FavoriteNotFoundException → HTTP 404 と type FAVORITE_NOT_FOUND が返ること"() {
        given:
        doThrow(new FavoriteNotFoundException(1L)).when(favoritesService).remove(anyLong(), anyLong())

        expect:
        mockMvc.perform(delete("/v1/contents/1/like"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath('$.type').value("FAVORITE_NOT_FOUND"))
    }

    def "postLike — 数値以外の contentId → HTTP 400 と type INVALID_PARAMETER が返ること"() {
        expect:
        mockMvc.perform(post("/v1/contents/abc/like"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath('$.type').value("INVALID_PARAMETER"))
    }
}
