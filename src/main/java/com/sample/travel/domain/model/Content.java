package com.sample.travel.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record Content(
        Long id,
        String title,
        String prefectureName,
        String countryName,
        String address,
        String distance,
        Integer price,
        LocalDateTime availableFrom,
        LocalDateTime availableTo,
        BigDecimal averageRating,
        Long reviewCount,
        boolean favorite,
        List<String> images
) {}
