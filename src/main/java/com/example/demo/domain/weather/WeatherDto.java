package com.example.demo.domain.weather;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherDto {
    private double temp;      // 기온 (T1H)
    private String location;  // 위치명
    private int sky;          // SKY
    private int pty;          // PTY
}
