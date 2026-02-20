package com.example.demo.domain.diary;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Diary {
    private Long id;
    private LocalDate diaryDate;
    private String title;
    private String content;
}
