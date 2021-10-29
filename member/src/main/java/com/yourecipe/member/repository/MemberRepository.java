package com.yourecipe.member.repository;

import com.yourecipe.member.model.Member;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface MemberRepository {
    // 회원정보 등록하기
    int joinMember(Member member);

    // 회원정보 가져오기
    Member findMember(int memberId);

    // 회원정보 수정하기
    int updateMember(Member member);

    // 회원정보 삭제하기
    int quitMember(int memberId);
}
