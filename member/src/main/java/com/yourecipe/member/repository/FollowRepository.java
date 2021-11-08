package com.yourecipe.member.repository;

import com.yourecipe.member.model.Follow;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository {
    // 팔로잉 정보 입력
    int addFolow(Follow follow);

    // 팔로잉 전체 조회
    List<Follow> selectFollowList(int memberId);

    // 팔로잉 삭제
    int deleteFollow(int followingId);

    // 팔로잉 전체 삭제
    int deleteFollowList(int memberId);
}
