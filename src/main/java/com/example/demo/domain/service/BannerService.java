package com.example.demo.domain.service;

import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneId;

@Service
public class BannerService {

    public int getBannerImageNo() {
        int hour = LocalTime.now(ZoneId.of("Asia/Seoul")).getHour();

        if (hour >= 7 && hour < 12) return 1;
        if (hour >= 12 && hour < 15) return 2;
        if (hour >= 15 && hour < 18) return 3;
        if (hour >= 18 && hour < 20) return 4;
        if (hour >= 20 && hour < 22) return 5;
        if (hour >= 2 && hour < 5) return 7;
        if (hour >= 5 && hour < 7) return 8;

        return 6; // 22~02
    }
}

