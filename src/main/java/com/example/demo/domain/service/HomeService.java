package com.example.demo.domain.service;

import com.example.demo.domain.Banner;
import com.example.demo.domain.Recommendation;
import com.example.demo.domain.BoxInfo;
import java.util.List;

public interface HomeService {

    Banner getMainBanner(); // 메인 배너 정보를 담은 Banner 객체 반환

    List<Recommendation> getRecommendations();

    List<BoxInfo> getBoxInfos();

    List<Banner> getAllBanners();

    Banner getBanner(Long id);

    void createBanner(Banner banner);

    void updateBanner(Banner banner);

    void deleteBanner(Long id);

    List<Recommendation> getAllRecommendations();

    Recommendation getRecommendation(Long id);

    void createRecommendation(Recommendation r);

    void updateRecommendation(Recommendation r);

    void deleteRecommendation(Long id);

    List<BoxInfo> getAllBoxInfos();

    BoxInfo getBoxInfo(Long id);

    void createBoxInfo(BoxInfo info);

    void updateBoxInfo(BoxInfo info);

    void deleteBoxInfo(Long id);

}


