package com.sample.travel.domain.model;

import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record Content(
        long id,
        @NonNull String title,
        @NonNull String prefectureName,
        @NonNull String countryName,
        @NonNull String address,
        @NonNull String distance,
        int price,
        @NonNull LocalDateTime availableFrom,
        @NonNull LocalDateTime availableTo,
        BigDecimal averageRating,
        long reviewCount,
        boolean favorite,
        @NonNull List<String> images
) {}
