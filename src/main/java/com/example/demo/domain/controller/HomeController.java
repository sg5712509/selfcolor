package com.example.demo.domain.controller;

import com.example.demo.domain.Banner;
import com.example.demo.domain.CustomPropertyConfig;
import com.example.demo.domain.service.BannerService;
import com.example.demo.domain.service.HomeService;
import com.example.demo.domain.service.WeatherBannerService;
import com.example.demo.domain.weather.WeatherDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import jakarta.servlet.http.HttpSession;
import com.example.demo.domain.oauth.OAuthService;
import com.example.demo.domain.oauth.UserService;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;
    private final OAuthService oAuthService;
    private final UserService userService;
    private final BannerService bannerService;
    private final WeatherBannerService weatherBannerService;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {

        Banner banner = homeService.getMainBanner();
        /*결국 DB 에서 banner 테이블 가져 오는 셈*/

        if (banner == null) {
            banner = new Banner();
        }

        // 날씨별 이미지
        int weatherCode = 4; // 기본값(맑음). 실제론 weatherService 결과로 세팅
        banner.setImageUrl(weatherBannerService.getBannerImageUrl(weatherCode));
        // banner 의 imageUrl 을 수정한다. (weatherBannerService 의 getBannerImageUrl 메서드의 값을 넣는다.)

        // ✅ 시간 별 이미지 선택 (Asia/Seoul)
        int imgNo = bannerService.getBannerImageNo();
        imgNo = Math.max(1, Math.min(imgNo, 6)); // 안전장치
        banner.setLeftImage("/images/time/" + imgNo + ".png");

        model.addAttribute("banner", banner);
        // Thymeleaf 문법으로 <img th:src="${banner.imageUrl}">

        model.addAttribute("recs", homeService.getRecommendations());

        model.addAttribute("infos", homeService.getBoxInfos());

        model.addAttribute("kakaoLoginUrl", "/oauth/kakao");

        model.addAttribute("naverLoginUrl", "/oauth/naver");

        model.addAttribute("heroImageUrl", "/images/hero.jpg");

        // ✅ 로그인 정보(세션) → 화면으로 전달
        model.addAttribute("loginUserId", session.getAttribute("LOGIN_USER_ID"));
        model.addAttribute("loginNickname", session.getAttribute("LOGIN_NICKNAME"));

        // home() 안의 날씨 부분만 이렇게
        WeatherDto weatherDto = new WeatherDto(0, "현재 위치", 1, 0);
        model.addAttribute("weather", weatherDto);

        return "home";
    }

    private final CustomPropertyConfig customPropertyConfig;
    // application.properties 에 있는 설정값이 들어있는 클래스

    @GetMapping("/oauth/kakao")
    public RedirectView kakaoStart() {
        String url = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + enc(customPropertyConfig.getKakaoClientId())
                // properties 에서
                // custom.property.kakao-client-id=6663d2506ab9bb404884c489cbe3bf9e
                + "&redirect_uri=" + enc(customPropertyConfig.getKakaoRedirectUri())
                // properties 에서
                // custom.property.kakao-redirect-uri=http://localhost:8080/user/login/kakao
                + "&response_type=code"

                + "&prompt=login";

        return new RedirectView(url);

    }

    @GetMapping("/user/login/kakao")
    public String kakaoCallback(
            @RequestParam(value = "code", required = false) String code, /*카카오가 준 인가 코드*/
            // &response_type=code 로 카카오에게 요청한 인가 코드가 이곳에 적혀 있다.
            HttpSession session, /* session */
            @RequestParam(value = "error", required = false) String error,  /*?error=access_denied 인 상황을 가져온다*/
            @RequestParam(value = "error_description", required = false) String errorDesc /*에러 상세 설명*/) {

        // 1) 카카오 로그인 실패 or 취소한 경우
        if (error != null) {
            System.out.println("[KAKAO] error=" + error + ",desc=" + errorDesc);
            return "redirect:/?loginError=kakao";
            /*실패하면 그냥 메인으로 돌려보냄*/
        }

        if (code == null) {
            System.out.println("[KAKAO] no code");
            return "redirect:/?loginError=kakao_no_code";
        }

        // 2) 인가 코드를 토큰으로 교환
        var token = oAuthService.exchangeKakaoToken(code);

        // 3) 토큰으로 카카오유저프로필을 조회
        var profile = oAuthService.getKakaoProfile(token.getAccess_token());

        // 4) 프로필에서 고유 id 따와서 담기
        String socialId = String.valueOf(profile.getId());

        // 5) 이메일 조회
        String email = profile.getKakao_account() != null ? profile.getKakao_account().getEmail() : null;

        String nickname = null;

        // 6) 닉네임 추출
        if (profile.getKakao_account() != null && profile.getKakao_account().getProfile() != null) {
            nickname = profile.getKakao_account().getProfile().getNickname();
        }

        // 7) socialId, email, nickname 을 업데이트 하기
        var user = userService.upsertSocialUser("KAKAO", socialId, email, nickname);

        // 8) user 의 id 를 session 에 추가
        session.setAttribute("LOGIN_USER_ID", user.getId());

        // 9) user 의 nickname 을 session 에 추가
        session.setAttribute("LOGIN_NICKNAME", user.getNickname());

        // 10) main 화면으로 이동
        return "redirect:/";
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate(); // 세션 통째로 폐기

        String url = "https://kauth.kakao.com/oauth/logout"
                + "?client_id=" + enc(customPropertyConfig.getKakaoClientId())
                + "&logout_redirect_uri=" + enc("http://localhost:8080/");
        // properties 로 뺴야함 (배포시 터짐)
        return new RedirectView(url);
    }

    // =======================네이버 서버랑 연결되는 곳=======================
    @GetMapping("/oauth/naver")
    // GET /oauth/naver 요청 시 네이버 로그인 시작
    public RedirectView naverStart(HttpSession session) {
        // 네이버 인가 URL 로 리다이렉트할 메서드
        String state = UUID.randomUUID().toString();
        session.setAttribute("OAUTH_NAVER_STATE", state);
        // OAuth 의 state 파라미터
        // CSRF 방어용: 시작할 때 랜덤값 만들고, 콜백 때 같은 값이 돌아왔는지 검증 해야 함.
        // 근데 이 코드에선 "생성만 하고 저장/검증"을 안 해서 반쪽짜리임(보안적으로 별로)
        String url = "https://nid.naver.com/oauth2.0/authorize"
                // 네이버 OAuth 인가 엔드포인트
                + "?response_type=code"
                // 네이버도 Authorization Code 방식 사용
                + "&client_id=" + enc(customPropertyConfig.getNaverClientId())
                // 네이버 앱 client_id 붙임
                + "&redirect_uri=" + enc(customPropertyConfig.getNaverRedirectUri())
                // 네이버가 돌아올 콜백 URL
                + "&state=" + enc(state);
        // 방금 만든 state 를 붙여서 보냄.
        return new RedirectView(url);
        // 브라우저를 네이버 로그인 페이지로 보냄
    }

    @GetMapping("/user/login/naver") // 이것도 properties 값이랑 동일해야 함
    public String naverCallback(@RequestParam("code") String code,
                                @RequestParam("state") String state,
                                HttpSession session,
                                @RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "error_description", required = false) String errorDesc) {
        if (error != null) {
            System.out.println("[NAVER] error=" + error + ", desc=" + errorDesc);
            return "redirect:/?loginError=naver";
        }

        String saved = (String) session.getAttribute("OAUTH_NAVER_STATE");
        session.removeAttribute("OAUTH_NAVER_STATE");
        if (saved == null || !saved.equals(state)) {
            return "redirect:/?loginError=state";
        }

        var token = oAuthService.exchangeNaverToken(code, state);
        var profile = oAuthService.getNaverProfile(token.getAccess_token());

        String socialId = profile.getResponse().getId();
        String email = profile.getResponse().getEmail();
        String nickname = profile.getResponse().getNickname();

        var user = userService.upsertSocialUser("NAVER", socialId, email, nickname);
        // 네이버가 돌려주는 code와 state 를 둘 다 받음
        // 정상이라면 여기서 state 가 내가 보낸 값과 같은지 검증해야 함 (근데 지금 검증 로직 없음)
        System.out.println("[NAVER] code=" + code + ", state=" + state);

        session.setAttribute("LOGIN_USER_ID", user.getId());
        session.setAttribute("LOGIN_NICKNAME", user.getNickname());
        // 로그 출력.
        //code로 토큰받고 -> 유저정보 받아서 -> 로그인 처리
        // 카카오와 동일하게
        // 1. code로 access_token 받기
        // 2. 토큰으로 유저정보 조회
        // 3. 우리 서비스 로그인 처리
        return "redirect:/";
        // 홈으로 리다이렉트
    }


    private String enc(String v)
    // 문자열은 URL 쿼리 파라미터로 안전하게 만들기 위한 함수.
    {
        return URLEncoder.encode(v, StandardCharsets.UTF_8);
    }
    // UTF-8로 URL 인코딩
    // 예: https://.../callback?x=1&y=2 같은 문자열이 redirect-uri로 들어가면 & 같은게 깨지니까 인코딩이 필요함.

    /*1) 네이버 state 를 만들었는데 서버에 저장도 안 하고 검증도 안 함 -> CSRF 방어가 안됨. 2) 카카오/네이버 모두 callback에서 에러 파라미터(error, error_description) 케이스 처리 없음. 3) 아직 토큰 교환/유저 조회/로그인 처리가 없어서 "OAuth 로그인"이 안성된 게 아님. 지금은 "인가 코드 출력" 데모 수준.*/







/*    @RestController
    @RequiredArgsConstructor
    public class WeatherController {

        private final WeatherService weatherService;

        @GetMapping("/api/weather")
        public WeatherResponse weather(
                @RequestParam double lat,
                @RequestParam double lon
        ) {
            return weatherService.getWeather(lat, lon);
        }
    }*/

}
