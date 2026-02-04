package com.example.demo.domain.weather;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Grid {
    private int nx;
    private int ny;
    private String locationName;
}

