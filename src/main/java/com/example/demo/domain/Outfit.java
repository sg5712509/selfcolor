package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Outfit {
    private Long id;
    private String title;
    private String summary;
    private String imageUrl; // ✅ 여기 중요 (DB는 image_url)
}

