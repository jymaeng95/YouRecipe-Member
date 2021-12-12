package com.yourecipe.member.follow;

import com.yourecipe.member.model.Follow;
import com.yourecipe.member.repository.FollowRepository;
import com.yourecipe.member.service.FollowService;
import com.yourecipe.member.service.FollowServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {
    @Mock
    private FollowRepository followRepository;

    @InjectMocks
    private FollowServiceImpl followService;

    @Test
    @DisplayName("팔로우 서비스 : 팔로우 추가(성공)")
    void 팔로우_성공() {
        //given
        Follow follow = createFollowList();
        given(followRepository.addFollow(any())).willReturn(1);

        //when
        boolean rst = followService.doFollow(follow);

        //then
        assertThat(rst).isTrue();
    }

    @Test
    @DisplayName("팔로우 서비스 : 팔로우 추가(실패)")
    void 팔로우_실패() {
        //given
        Follow follow = createFollowList();
        given(followRepository.addFollow(any())).willReturn(0);

        //when
        boolean rst = followService.doFollow(follow);

        //then
        assertThat(rst).isFalse();
    }

    @Test
    @DisplayName("팔로우 서비스 : 팔로우 리스트 조회(성공)")
    void 팔로우_리스트_조회_성공() {
        //given
        List<Follow> followList = createFollowLists();
        given(followRepository.selectFollowList(2)).willReturn(followList);

        //when
        Optional<List<Follow>> follows = Optional.ofNullable(followService.getFollowList(2));

        //then
        if(follows.isPresent()){
            assertThat(follows).isNotNull();
            assertThat(follows.get().get(0).getMemberId()).isEqualTo(followList.get(0).getMemberId());
            assertThat(follows.get().get(0).getFeedId()).isEqualTo(followList.get(0).getFeedId());
            assertThat(follows.get().get(1).getMemberId()).isEqualTo(followList.get(1).getMemberId());
            assertThat(follows.get().get(1).getFeedId()).isEqualTo(followList.get(1).getFeedId());
//            assertThat(follows.get(0).getMemberId()).isEqualTo(2);
//            assertThat(follows.get(0).getFeedId()).isEqualTo(1);
//            assertThat(follows.get(1).getMemberId()).isEqualTo(2);
//            assertThat(follows.get(1).getFeedId()).isEqualTo(3);
        }
    }

    @Test
    @DisplayName("팔로우 서비스 : 팔로우 리스트 조회(실패)")
    void 팔로우_리스트_조회_실패() {
        //given
        given(followRepository.selectFollowList(0)).willReturn(null);

        //when
        Optional<List<Follow>> follows = Optional.ofNullable(followService.getFollowList(0));

        //then
        assertThat(follows.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("팔로우 서비스 : 팔로잉 삭제, 피드 언팔로잉(성공)")
    void 팔로잉_삭제_성공() {
        //given
        given(followRepository.deleteFollow(any())).willReturn(1);

        //when
        boolean rst = followService.doUnfollow(2,1);

        //then
        assertThat(rst).isTrue();
    }

    @Test
    @DisplayName("팔로우 서비스 : 팔로잉 삭제, 피드 언팔로잉(실패)")
    void 팔로잉_삭제_실패() {
        //given
        given(followRepository.deleteFollow(any())).willReturn(0);

        //when
        boolean rst = followService.doUnfollow(2,0);

        //then
        assertThat(rst).isFalse();
    }

    @Test
    @DisplayName("팔로우 서비스 : 팔로우 리스트 삭제(성공)")
    void 팔로우_리스트_삭제_성공() {
        //givne
        given(followRepository.deleteFollowList(anyInt())).willReturn(1);

        //when
        boolean rst = followService.clearFollowList(1);

        //then
        assertThat(rst).isTrue();
    }

    @Test
    @DisplayName("팔로우 서비스 : 팔로우 리스트 삭제(실패)")
    void 팔로우_리스트_삭제_실패() {
        //given
        given(followRepository.deleteFollowList(anyInt())).willReturn(0);

        //when
        boolean rst = followService.clearFollowList(0);

        //then
        assertThat(rst).isFalse();
    }

    private static Follow createFollowList() {
        return Follow.builder()
                .memberId(2)
                .feedId(1)
                .build();
    }

    private static List<Follow> createFollowLists() {
        Follow follow1 = Follow.builder()
                .memberId(2)
                .feedId(1)
                .build();

        Follow follow2 = Follow.builder()
                .memberId(2)
                .feedId(3)
                .build();

        return List.of(follow1,follow2);
    }
}
