package com.yourecipe.member.service;

import com.yourecipe.member.model.Scrap;

import java.util.List;
import java.util.Optional;

public interface ScrapService {
    // 레시피 스크랩 등록
    boolean doScrap(Scrap scrap);

    // 레시피 스크랩 해제
    boolean cancelScrap(int recipeId);

    // 레시피 리스트 전체 삭제
    boolean clearScrapList(int memberId);

    // 레시피 스크랩 리스트 가져오기
    Optional<List<Scrap>> getScrapList(int memberId);
}
