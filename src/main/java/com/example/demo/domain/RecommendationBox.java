package com.example.demo.domain;

import lombok.Data;

@Data
public class RecommendationBox {
    private Long id;
    private Long recommendationId;
    private Integer sortOrder;
    /*private String bgColorHex;*/   // "#EFD7DB"
    private String imageUrl;   // "/images/xxx.png" (선택)
}

