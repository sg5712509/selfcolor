package com.example.demo.domain.oauth;

import lombok.Data;

@Data
public class NaverProfileResponse {
    private String resultcode;
    private String message;
    private NaverUser response;

    @Data
    public static class NaverUser {
        private String id;
        private String email;
        private String nickname;
    }
}

