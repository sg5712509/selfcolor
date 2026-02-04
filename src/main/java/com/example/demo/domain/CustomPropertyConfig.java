package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@Setter
@ConfigurationProperties(prefix = "custom.property")
public class CustomPropertyConfig {

    private String kakaoClientId;
    // 원래는 kakao-client-id, 스프링 규칙에 의하여 kakaoClientId 가 됨.

    private String kakaoRedirectUri;

    private String naverClientId;
    private String naverClientSecret;
    private String naverRedirectUri;
    private String weatherKey;
    private String kakaoRestKey;
}
