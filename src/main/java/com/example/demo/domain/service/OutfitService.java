package com.example.demo.domain.service;

import com.example.demo.domain.Outfit;
import com.example.demo.domain.mapper.OutfitMapper;
import org.springframework.stereotype.Service;

@Service
public class OutfitService {

    private final OutfitMapper outfitMapper;

    public OutfitService(OutfitMapper outfitMapper) {
        this.outfitMapper = outfitMapper;
    }

    public Outfit getTodayOutfit() {
        return outfitMapper.findRandomOne(); // 일단은 매번 랜덤
    }
}

