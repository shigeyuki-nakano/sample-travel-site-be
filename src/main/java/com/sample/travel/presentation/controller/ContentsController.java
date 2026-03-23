package com.sample.travel.presentation.controller;

import com.sample.travel.domain.model.Content;
import com.sample.travel.domain.service.ContentsService;
import com.sample.travel.presentation.api.ContentsApi;
import com.sample.travel.presentation.dto.ContentResponse;
import com.sample.travel.presentation.dto.ContentsListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ContentsController implements ContentsApi {

    private final ContentsService contentsService;

    /**
     * 宿泊施設の一覧を取得し、HTTP 200 でラップして返す。
     *
     * @return HTTP 200 と宿泊施設一覧レスポンス
     */
    @Override
    public ResponseEntity<ContentsListResponse> getContents() {
        List<ContentResponse> responses = contentsService.findAll().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(new ContentsListResponse().contents(responses));
    }

    private ContentResponse toResponse(Content content) {
        return new ContentResponse()
                .id(content.id())
                .title(content.title())
                .location(content.prefectureName() + "、" + content.countryName())
                .distance(content.distance())
                .dates(formatDates(content))
                .price(content.price())
                .rating(content.averageRating())
                .reviewCount(content.reviewCount())
                .isFavorite(content.favorite())
                .images(content.images() != null ? content.images() : List.of())
                .isGuestFavorite(isGuestFavorite(content));
    }

    private String formatDates(Content content) {
        int month = content.availableFrom().getMonthValue();
        int fromDay = content.availableFrom().getDayOfMonth();
        int toDay = content.availableTo().getDayOfMonth();
        return month + "月" + fromDay + "日〜" + toDay + "日";
    }

    private boolean isGuestFavorite(Content content) {
        return content.averageRating() != null
                && content.averageRating().compareTo(new BigDecimal("4.9")) >= 0
                && content.reviewCount() >= 1;
    }
}
