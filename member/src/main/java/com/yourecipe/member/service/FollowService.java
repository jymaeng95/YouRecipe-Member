package com.yourecipe.member.service;

import com.yourecipe.member.model.Follow;

import java.util.List;

public interface FollowService {
    // 팔로우 등록
    boolean doFollow(Follow follow);

    // 팔로우 해제
    boolean doUnfollow(int memberId, int feedId);

    // 팔로우 리스트 삭제
    boolean clearFollowList(int memberId);

    // 팔로우 리스트 조회
    List<Follow> getFollowList(int memberId);
}
