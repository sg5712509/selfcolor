package com.example.demo.domain.weather;

import lombok.Data;
import java.util.List;

@Data
public class KakaoCoord2AddressResponse {
    private List<Document> documents;

    @Data
    public static class Document {
        private Address address;
        private RoadAddress road_address;

        @Data
        public static class Address {
            private String address_name; // "대구광역시 수성구 ..."
            private String region_1depth_name; // "대구광역시"
            private String region_2depth_name; // "수성구"
            private String region_3depth_name; // "범어동"
        }

        @Data
        public static class RoadAddress {
            private String address_name;
        }
    }
}

