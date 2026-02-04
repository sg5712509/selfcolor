package com.example.demo.domain.weather;

import lombok.Data;
import java.util.List;

@Data
public class KmaUltraSrtFcstResponse {

    private Response response;

    @Data
    public static class Response {
        private Header header;
        private Body body;
    }

    @Data
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Data
    public static class Body {
        private Items items;
    }

    @Data
    public static class Items {
        private List<Item> item;
    }

    @Data
    public static class Item {
        private String category;   // T1H, SKY, PTY 등
        private String fcstDate;   // yyyyMMdd
        private String fcstTime;   // HHmm
        private String fcstValue;  // 값
    }
}

