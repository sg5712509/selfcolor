package com.example.demo.domain.weather;

import com.example.demo.domain.CustomPropertyConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;




import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final RestTemplate restTemplate;
    private final CustomPropertyConfig props;
    private final ReverseGeocodeService reverseGeocodeService;

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter YMD = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter HM = DateTimeFormatter.ofPattern("HHmm");

    public WeatherDto getWeather(double lat, double lon) {
        Grid grid = GridConverter.toGrid(lat, lon);

        ZonedDateTime now = ZonedDateTime.now(KST);

        // 초단기예보는 보통 매시 30분 생성(실제 반영까지 지연 있음)이라 안전하게 45분 컷
        ZonedDateTime base = (now.getMinute() < 45)
                ? now.minusHours(1).withMinute(30).withSecond(0).withNano(0)
                : now.withMinute(30).withSecond(0).withNano(0);

        String baseDate = base.format(YMD);
        String baseTime = base.format(HM); // 보통 HH30

        String url = UriComponentsBuilder
                .fromUriString("https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst")
                .queryParam("serviceKey", props.getWeatherKey())
                .queryParam("dataType", "JSON")
                .queryParam("numOfRows", 200)
                .queryParam("pageNo", 1)
                .queryParam("base_date", baseDate)
                .queryParam("base_time", baseTime)
                .queryParam("nx", grid.getNx())
                .queryParam("ny", grid.getNy())
                .build(true)
                .toUriString();

        KmaUltraSrtFcstResponse res = restTemplate.getForObject(url, KmaUltraSrtFcstResponse.class);

        if (res == null
                || res.getResponse() == null
                || res.getResponse().getBody() == null
                || res.getResponse().getBody().getItems() == null
                || res.getResponse().getBody().getItems().getItem() == null) {
            return new WeatherDto(0, grid.getLocationName(), 1, 0);
        }

        List<KmaUltraSrtFcstResponse.Item> items = res.getResponse().getBody().getItems().getItem();

        // fcstDate+fcstTime 기준으로 "지금 이후 가장 가까운 시간" 한 세트를 고름
        ZonedDateTime target = now.withSecond(0).withNano(0);

        Map<ZonedDateTime, Map<String, String>> bucket = new HashMap<>();
        for (var it : items) {
            try {
                LocalDate d = LocalDate.parse(it.getFcstDate(), YMD);
                LocalTime t = LocalTime.parse(it.getFcstTime(), HM);
                ZonedDateTime dt = ZonedDateTime.of(d, t, KST);

                bucket.computeIfAbsent(dt, k -> new HashMap<>())
                        .put(it.getCategory(), it.getFcstValue());
            } catch (Exception ignored) {}
        }

        ZonedDateTime best = bucket.keySet().stream()
                .filter(dt -> !dt.isBefore(target))
                .min(Comparator.naturalOrder())
                .orElseGet(() -> bucket.keySet().stream().max(Comparator.naturalOrder()).orElse(null));

        if (best == null) {
            return new WeatherDto(0, grid.getLocationName(), 1, 0);
        }

        Map<String, String> v = bucket.get(best);

        double temp = parseDouble(v.get("T1H"), 0);
        int sky = parseInt(v.get("SKY"), 1);
        int pty = parseInt(v.get("PTY"), 0);



        String locationName = reverseGeocodeService.getLocationName(lat, lon);
        return new WeatherDto(temp, locationName, sky, pty);

    }

    private double parseDouble(String s, double def) {
        try { return Double.parseDouble(s); } catch (Exception e) { return def; }
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }


}
