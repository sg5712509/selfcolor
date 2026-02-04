package com.example.demo.domain.service;

import com.example.demo.domain.Banner;
import com.example.demo.domain.Recommendation;
import com.example.demo.domain.BoxInfo;
import com.example.demo.domain.mapper.HomeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final HomeMapper homeMapper;

    @Override
    public Banner getMainBanner() {
        return homeMapper.findMainBanner();
    }
    // 컨트롤러에서 service 에 요청한 getMainBanner 메서드를 homeMapper 에 넘김


    @Override
    public List<Recommendation> getRecommendations() {
        return homeMapper.findRecommendations();
    }

    @Override
    public List<BoxInfo> getBoxInfos() {
        return homeMapper.findBoxInfos();
    }

    @Override
    public List<Banner> getAllBanners() {
        return homeMapper.findAllBanners();
    }
    @Override
    public Banner getBanner(Long id) {
        return homeMapper.findBannerById(id);
    }
    @Override
    public void createBanner(Banner banner) {
        homeMapper.insertBanner(banner);
    }
    @Override
    public void updateBanner(Banner banner) {
        homeMapper.updateBanner(banner);
    }
    @Override
    public void deleteBanner(Long id) {
        homeMapper.deleteBanner(id);
    }

    @Override public List<Recommendation> getAllRecommendations() {
        return homeMapper.findAllRecommendations();
    }
    @Override public Recommendation getRecommendation(Long id) {
        return homeMapper.findRecommendationById(id);
    }
    @Override public void createRecommendation(Recommendation r) {
        homeMapper.insertRecommendation(r);
    }
    @Override public void updateRecommendation(Recommendation r) {
        homeMapper.updateRecommendation(r);
    }
    @Override public void deleteRecommendation(Long id) {
        homeMapper.deleteRecommendation(id);
    }

    @Override public List<BoxInfo> getAllBoxInfos() {
        return homeMapper.findAllBoxInfos();
    }
    @Override public BoxInfo getBoxInfo(Long id) {
        return homeMapper.findBoxInfoById(id);
    }
    @Override public void createBoxInfo(BoxInfo info) {
        homeMapper.insertBoxInfo(info);
    }
    @Override public void updateBoxInfo(BoxInfo info) {
        homeMapper.updateBoxInfo(info);
    }
    @Override public void deleteBoxInfo(Long id) {
        homeMapper.deleteBoxInfo(id);
    }
}

