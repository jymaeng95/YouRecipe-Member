package com.yourecipe.member.service;

import com.yourecipe.member.model.Member;
import com.yourecipe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor    // 생성자 주입
@Transactional
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;

    @Override
    public boolean  signUpMember(Member member) {
        return memberRepository.joinMember(member) > 0;
    }

    @Override
    public Member findMemberById(int memberId) {
        return memberRepository.findMember(memberId);
    }

    @Override
    public boolean editMember(Member member) {
        return memberRepository.updateMember(member) > 0;
    }

    @Override
    public boolean quitMember(int memberId) {
        return memberRepository.quitMember(memberId) > 0;
    }
}
