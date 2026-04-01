package com.sample.travel.infrastructure.entity;

import lombok.Data;

@Data
public class ContentImageEntity {

    private Long contentId;
    private String imageUrl;
    private Integer sortOrder;
}
