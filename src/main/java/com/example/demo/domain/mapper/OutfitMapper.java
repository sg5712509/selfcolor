package com.example.demo.domain.mapper;

import com.example.demo.domain.Outfit;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OutfitMapper {
    Outfit findRandomOne();
}
