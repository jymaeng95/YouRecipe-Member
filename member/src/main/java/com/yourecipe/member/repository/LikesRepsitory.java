package com.yourecipe.member.repository;

import com.yourecipe.member.model.Likes;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikesRepsitory {
    // 관심 카테고리 등록
    int addLikes(Likes likes);

    // 관심 카테고리 해제
    int deleteLikes(int categoryId);

    // 관심 카테고리 전체 삭제
    int deleteAllLikes(int memberId);

    // 관심 카테고리 리스트 가져오기
    List<Likes> selectLikesList(int memberId);
}
