package com.sample.travel.infrastructure.entity;

import com.sample.travel.domain.model.Content;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ContentEntity {

    private Long id;
    private String title;
    private String prefectureName;
    private String countryName;
    private String address;
    private String distance;
    private Integer price;
    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;
    private BigDecimal averageRating;
    private Long reviewCount;
    private boolean favorite;

    /**
     * エンティティをドメインモデルに変換する。
     *
     * @param images 紐付けるコンテンツ画像URLのリスト（sort_order 昇順）
     * @return {@link Content} ドメインモデル
     */
    public Content toContent(List<String> images) {
        return new Content(
                id, title, prefectureName, countryName,
                address, distance, price,
                availableFrom, availableTo,
                averageRating, reviewCount, favorite,
                images
        );
    }
}
