package com.example.demo.domain.service;

import org.springframework.stereotype.Service;

@Service
public class WeatherBannerService {

    public String getBannerImageUrl(int weatherCode) {
        return switch (weatherCode) {
            case 1 -> "/images/weather/1.png";
            case 2 -> "/images/weather/2.png";
            case 3 -> "/images/weather/3.png";
            case 4 -> "/images/weather/4.png";
            case 5 -> "/images/weather/5.png";
            case 6 -> "/images/weather/6.png";
            default -> "/images/weather/4.png";
        };
    }
}

