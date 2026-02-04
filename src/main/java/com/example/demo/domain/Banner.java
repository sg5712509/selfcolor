package com.example.demo.domain;

import lombok.Data;

@Data
public class Banner {
    private Long id;
    private String title;
    private String subtitle;
    private String imageUrl; // /images/banner-right.jpg
    private String leftImage;
}

