package com.example.demo.domain.diary;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DiaryMapper {
    Diary findByDate(@Param("date") LocalDate date);

    void insert(@Param("date") LocalDate date,
                @Param("title") String title,
                @Param("content") String content);

    void update(@Param("date") LocalDate date,
                @Param("title") String title,
                @Param("content") String content);

    List<Diary> findByMonth(@Param("start") LocalDate start,
                            @Param("end") LocalDate end);
}
