package com.example.demo.domain.oauth;

import com.example.demo.domain.CustomPropertyConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final RestTemplate restTemplate;
    private final CustomPropertyConfig props;

    public OAuthTokenResponse exchangeKakaoToken(String code) {
        System.out.println("[KAKAO] client_id=" + props.getKakaoClientId());
        System.out.println("[KAKAO] redirect_uri=" + props.getKakaoRedirectUri());

        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", props.getKakaoClientId());
        form.add("redirect_uri", props.getKakaoRedirectUri());
        form.add("code", code);

        HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<>(form, headers);
        return restTemplate.postForEntity(url, req, OAuthTokenResponse.class).getBody();
    }

    public KakaoProfileResponse getKakaoProfile(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), KakaoProfileResponse.class).getBody();
    }














    public OAuthTokenResponse exchangeNaverToken(String code, String state) {
        // 네이버는 GET 쿼리로도 동작하는 케이스가 많음
        String url = "https://nid.naver.com/oauth2.0/token"
                + "?grant_type=authorization_code"
                + "&client_id=" + props.getNaverClientId()
                + "&client_secret=" + props.getNaverClientSecret()
                + "&code=" + code
                + "&state=" + state;

        return restTemplate.getForObject(url, OAuthTokenResponse.class);
    }

    public NaverProfileResponse getNaverProfile(String accessToken) {
        String url = "https://openapi.naver.com/v1/nid/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), NaverProfileResponse.class).getBody();
    }
}

