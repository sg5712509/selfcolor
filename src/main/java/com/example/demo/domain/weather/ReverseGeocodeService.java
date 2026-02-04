package com.example.demo.domain.weather;

import com.example.demo.domain.CustomPropertyConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class ReverseGeocodeService {

    private final RestTemplate restTemplate;
    private final CustomPropertyConfig props;

    public String getLocationName(double lat, double lon) {
        String url = UriComponentsBuilder
                .fromUriString("https://dapi.kakao.com/v2/local/geo/coord2address.json")
                .queryParam("x", lon)
                .queryParam("y", lat)
                .build(true)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + props.getKakaoRestKey());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoCoord2AddressResponse> res =
                restTemplate.exchange(url, HttpMethod.GET, entity, KakaoCoord2AddressResponse.class);

        var body = res.getBody();
        if (body == null || body.getDocuments() == null || body.getDocuments().isEmpty()) return "현재 위치";

        var doc = body.getDocuments().get(0);
        if (doc.getRoad_address() != null && doc.getRoad_address().getAddress_name() != null) {
            return doc.getRoad_address().getAddress_name();
        }
        if (doc.getAddress() != null) {
            // 원하는 수준으로 조합 (시/구/동)
            return doc.getAddress().getRegion_1depth_name() + " " +
                    doc.getAddress().getRegion_2depth_name() + " " +
                    doc.getAddress().getRegion_3depth_name();
        }
        return "현재 위치";
    }
}

