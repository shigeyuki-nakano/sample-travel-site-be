package com.sample.travel.infrastructure.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
}
