package com.example.demo.domain;

import lombok.Data;

import java.util.List;

@Data
public class Recommendation {
    private Long id;
    private String name;
    private String url;
    private String seller;

    private String bgColorHex;
    private List<RecommendationBox> boxes;
}

