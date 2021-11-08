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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
        given(followRepository.addFolow(any())).willReturn(1);

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
        given(followRepository.addFolow(any())).willReturn(0);

        //when
        boolean rst = followService.doFollow(follow);

        //then
        assertThat(rst).isFalse();
    }

    @Test
    @DisplayName("팔로우 서비스 : 팔로우 리스트 조회(성공)")
    void 팔로우_리스트_조회_성공() {
        //given
        given(followRepository.selectFollowList(2)).willReturn(createFollowLists());

        //when
        List<Follow> followList = createFollowLists();

        //then
        assertThat(followList).isNotNull();
        assertThat(followList.get(0).getMemberId()).isEqualTo(2);
        assertThat(followList.get(0).getFeedId()).isEqualTo(1);
        assertThat(followList.get(1).getMemberId()).isEqualTo(2);
        assertThat(followList.get(1).getFeedId()).isEqualTo(3);
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
