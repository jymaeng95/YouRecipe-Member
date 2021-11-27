package com.yourecipe.member.service;

import com.yourecipe.member.model.Follow;
import com.yourecipe.member.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowServiceImpl implements FollowService{
    private final FollowRepository followRepository;

    @Override
    public boolean doFollow(Follow follow) {
        return followRepository.addFollow(follow) > 0;
    }

    @Override
    public boolean doUnfollow(int feedId) {
        return followRepository.deleteFollow(feedId) > 0;
    }

    @Override
    public boolean clearFollowList(int memberId) {
        return followRepository.deleteFollowList(memberId) > 0;
    }

    @Override
    public List<Follow> getFollowList(int memberId) {
        return followRepository.selectFollowList(memberId);
    }
}
