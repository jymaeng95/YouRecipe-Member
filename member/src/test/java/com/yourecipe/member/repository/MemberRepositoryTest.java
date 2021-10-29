package com.yourecipe.member.repository;

import com.yourecipe.member.model.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryTest {
    @Resource
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 레포지토리 : 회원 생성 테스트(성공)")
    void 회원생성_성공() {
        //given
        Member member = createMemberForTest();

        //when
        int result = memberRepository.joinMember(member);

        //then
        assertThat(result).isPositive();
    }

    // 테스트에 사용할 member 객체 생성
    private Member createMemberForTest() {
        return Member.builder()
                .email("zayson.maeng@gmail.com")
                .nickname("zayson")
                .profileImg("test.com")
                .build();
    }
}