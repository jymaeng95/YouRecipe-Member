package com.yourecipe.member.member;

import com.yourecipe.member.model.Member;
import com.yourecipe.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@Transactional  // 테스트 후 사용 DB 롤백
public class MemberRepositoryTests {

    @Autowired
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
