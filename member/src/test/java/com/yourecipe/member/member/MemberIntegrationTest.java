package com.yourecipe.member.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yourecipe.member.controller.MemberController;
import com.yourecipe.member.model.Member;
import com.yourecipe.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
public class MemberIntegrationTest {
    /*
     * 통합 테스트 flow
     * Controller -> Service -> Repository (Transactional rollback)
     */
    @Autowired
    private MockMvc mvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate template;

    @BeforeEach
    void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .modules(new JavaTimeModule())
                .build();
    }

    @Test
    @DisplayName("회원 통합 테스트 : 회원 가입(성공)")
    void 회원가입_성공() throws Exception {
        //given
        String url = "/member";
        Member member = createMemberForTest();

        String content = objectMapper.writeValueAsString(member);

        // Controller logic
        mvc.perform(MockMvcRequestBuilders
                .post(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        //when
        ResponseEntity<String> response = template.postForEntity(url, member, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Service Logic
        boolean result = memberService.signUpMember(member);
        assertThat(result).isTrue();
    }

   /* @Test
    @DisplayName("회원 통합 테스트 : 회원 가입(실패)")
    void 회원가입_실패() throws Exception {
        //given
        String url = "/member";
        Member member = createMemberForFailTest();

        String content =  objectMapper.writeValueAsString(member);

        mvc.perform(MockMvcRequestBuilders
        .post(url)
        .content(content)
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(print());

        ResponseEntity<String> response = template.postForEntity(url, member, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

        boolean result = memberService.signUpMember(member);
        assertThat(result).isFalse();
    }*/

    @Test
    @DisplayName("회원 통합 테스트 : 회원 조회(성공)")
    void 회원조회_성공() throws Exception {
        String url = "/member/2";

        mvc.perform(MockMvcRequestBuilders
        .get(url))
                .andExpect(status().isOk())
                .andDo(print());

        ResponseEntity<Member> response = template.getForEntity(url, Member.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        Optional<Member> member = Optional.ofNullable(memberService.findMemberById(2));
        if(member.isPresent()) {
            assertThat(2).isEqualTo(member.get().getMemberId());
            assertThat("zayson.maeng@gmail.com").isEqualTo(member.get().getEmail());
            assertThat("zayson").isEqualTo(member.get().getNickname());
            assertThat("test.com").isEqualTo(member.get().getProfileImg());
        }
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

    private Member createMemberForFailTest() {
        return Member.builder()
                .memberId(2)
                .email("zayson.maeng@gmail.com")
                .nickname("zayson")
                .profileImg("test.com")
                .build();
    }

}
