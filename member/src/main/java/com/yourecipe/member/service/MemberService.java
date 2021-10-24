package com.yourecipe.member.service;

import com.yourecipe.member.model.Member;

public interface MemberService {
    // 회원 가입
    boolean signUpMember(Member member);

    // 회원ID로 정보 가져오기
    Member findMemberById(int memberId);

    // 회원정보 수정
    boolean editMember(Member member);

    // 회원 탈퇴하기
    boolean quitMember(int memberId);
}
