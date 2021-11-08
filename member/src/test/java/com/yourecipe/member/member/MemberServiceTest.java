package com.yourecipe.member.member;


import com.yourecipe.member.model.Member;
import com.yourecipe.member.repository.MemberRepository;
import com.yourecipe.member.service.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("회원 서비스 : 회원 가입 테스트(성공)")
    void 회원가입_성공() {
        //given
        given(memberRepository.joinMember(any())).willReturn(1);
        Member member = createMemberForTest();

        //when
        boolean result = memberService.signUpMember(member);

        //then
        assertThat(result).isEqualTo(true);
    }

    @Test
    @DisplayName("회원 서비스 : 회원 가입 테스트(실패)")
    void 회원가입_실패() {
        //given
        given(memberRepository.joinMember(any())).willReturn(0);
        Member member = createMemberForTest();

        //when
        boolean result = memberService.signUpMember(member);

        //then
        assertThat(result).isEqualTo(false);
    }

    @Test
    @DisplayName("회원 서비스 : 회원 정보 조회 테스트(성공)")
    void 회원조회_성공() {
        //given
        Member member = createMemberForTest();
        given(memberRepository.findMember(1)).willReturn(member);

        //when
        Member findMember = memberService.findMemberById(1);

        //then
        assertThat(findMember).isNotNull();
        assertThat(findMember.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(findMember.getNickname()).isEqualTo(member.getNickname());
        assertThat(findMember.getProfileImg()).isEqualTo(member.getProfileImg());
    }

    @Test
    @DisplayName("회원 서비스 : 회원 정보 조회 테스트(실패)")
    void 회원조회_실패() {
        //given
        given(memberRepository.findMember(2)).willReturn(null);

        //when
        Member findMember = memberService.findMemberById(2);

        //then
        assertThat(findMember).isNull();
    }

    @Test
    @DisplayName("회원 서비스 : 회원 정보 수정 테스트(성공)")
    void 회원수정_성공() {
        //given
        given(memberRepository.updateMember(any())).willReturn(1);
        Member member = createMemberForTest();

        //when
        boolean result = memberService.editMember(member);

        //then
        assertThat(result).isEqualTo(true);
    }

    @Test
    @DisplayName("회원 서비스 : 회원 정보 수정 테스트(실패)")
    void 회원수정_실패() {
        //given
        given(memberRepository.updateMember(any())).willReturn(0);
        Member member = createMemberForTest();

        //when
        boolean result = memberService.editMember(member);

        //then
        assertThat(result).isEqualTo(false);
    }

    @Test
    @DisplayName("회원 서비스 : 회원 탈퇴 테스트(성공)")
    void 회원탈퇴_성공() {
        //given
        given(memberRepository.quitMember(1)).willReturn(1);
        Member quitMember = createMemberForTest();

        //when
        boolean result = memberService.quitMember(quitMember.getMemberId());

        //then
        assertThat(result).isEqualTo(true);
    }

    @Test
    @DisplayName("회원 서비스 : 회원 탈퇴 테스트(실패)")
    void 회원탈퇴_실패() {
        //given
        given(memberRepository.quitMember(2)).willReturn(0);
        Member quitMember = createMemberForTest();

        //when
        boolean result = memberService.quitMember(2);

        //then
        assertThat(result).isEqualTo(false);
    }

    // 테스트에 사용할 member 객체 생성
    private Member createMemberForTest() {
        return Member.builder()
                .memberId(1)
                .email("zayson.maeng@gmail.com")
                .nickname("zayson")
                .profileImg("test.com")
                .build();
    }
}
