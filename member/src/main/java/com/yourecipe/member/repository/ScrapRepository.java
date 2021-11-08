package com.yourecipe.member.repository;

import com.yourecipe.member.model.Scrap;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScrapRepository {
    // 레시피 스크랩 등록
    int addScrap(Scrap scrap);

    // 레시피 스크랩 해제
    int deleteScrap(int scrapId);

    // 레시피 리스트 전체 삭제
    int deleteAllScrap(int memberId);

    // 레시피 스크랩 리스트 가져오기
    List<Scrap> selectScrapList(int memberId);

}
