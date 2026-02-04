package com.example.demo.domain.mapper;

import com.example.demo.domain.Banner;
import com.example.demo.domain.Recommendation;
import com.example.demo.domain.BoxInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HomeMapper {

    Banner findMainBanner();
    // HomeMapper.xml 을 찾음

    List<Recommendation> findRecommendations();
    // 이 메서드는 Mapper.xml의 findRecommendations 와 같다.
    // Mapper.xml 의 id 가 findRecommendations 과 같은 걸 이용해 연결하고
    // 생성된 객체를 Recommendation 에 돌려준다.
    // xml 에 쓰여있는건 DB의 recommendation 에 id, name, url, seller 선택 후 id는 역순서대로, 상위 10개를 추리는 것이다.
    // 총 정리하면 이 말과 같다. DB 에서 recommendation 테이블의 데이터를 가져오는 SQL 이 Mapper.xml 에 정의되어 있다. 그 SQL 은 가장 최신 (id DESC) 10개의 행만 조회한다. 이 결과를 MyBatis 가 Recommendation 자바 객체로 만들어서 리스트(List<Recommendation>)로 반환한다. 그리고 Controller 에서 Model 에 recs 로 담아 Thymeleaf 로 보낸다.
    // return List.of {new Recommendation(1, "", "", "")} 형태가 3번 추가.

    List<BoxInfo> findBoxInfos();

    // Banner CRUD
    List<Banner> findAllBanners();
    Banner findBannerById(Long id);
    int insertBanner(Banner banner);
    int updateBanner(Banner banner);
    int deleteBanner(Long id);

    // Recommendation CRUD
    List<Recommendation> findAllRecommendations();
    Recommendation findRecommendationById(Long id);
    int insertRecommendation(Recommendation r);
    int updateRecommendation(Recommendation r);
    int deleteRecommendation(Long id);

    // BoxInfo CRUD
    List<BoxInfo> findAllBoxInfos();
    BoxInfo findBoxInfoById(Long id);
    int insertBoxInfo(BoxInfo info);
    int updateBoxInfo(BoxInfo info);
    int deleteBoxInfo(Long id);
}
