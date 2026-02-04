package com.example.demo.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private LocalDateTime createdAt;

    private String socialTypeCode; // KAKAO/NAVER
    private String socialId;

    private String toneType;
}

