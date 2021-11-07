package com.yourecipe.member.repository;

import com.yourecipe.member.model.Following;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowingRepository {
    // 팔로잉 정보 입력
    int addFollowing(Following following);

    // 팔로잉 전체 조회
    List<Following> getFollowingList(int memberId);

    // 팔로잉 삭제
    int deleteFollowing(int followingId);

    // 팔로잉 전체 삭제
    int deleteFollowingList(int memberId);
}
