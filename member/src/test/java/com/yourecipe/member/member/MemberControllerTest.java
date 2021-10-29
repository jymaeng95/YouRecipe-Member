package com.yourecipe.member.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yourecipe.member.controller.MemberController;
import com.yourecipe.member.model.Member;
import com.yourecipe.member.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MemberController.class)
@ExtendWith(SpringExtension.class)
public class MemberControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private MemberService memberService;

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
        objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .modules(new JavaTimeModule())
                .build();
    }


    @Test
    @DisplayName("회원 컨트롤러 : 회원 가입 시도(성공)")
    void 회원가입_성공() throws Exception {
        //given
        Member member = createMemberForTest();
        given(memberService.signUpMember(any())).willReturn(true);

        String content = objectMapper.writeValueAsString(member);

        //when, then
        mvc.perform(MockMvcRequestBuilders
                .post("/member")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    logger.info("[TEST RESPONSE CODE] : " + response.getStatus());
                    logger.info("[TEST RESPONSE CONTENT] : " + response.getContentAsString());
                });
    }

    @Test
    @DisplayName("회원 컨트롤러 : 회원 가입 시도(실패)")
    void 회원가입_실패() throws Exception {
        //given
        Member member = createMemberForFailTest();
        given(memberService.signUpMember(any())).willReturn(false);

        String content = objectMapper.writeValueAsString(member);

        //when, then
        mvc.perform(MockMvcRequestBuilders
        .post("/member")
        .content(content)
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> {
                    MockHttpServletResponse response = result.getResponse();
                    logger.info("[TEST RESPONSE CODE] : " + response.getStatus());
                    logger.info("[TEST RESPONSE CONTENT] : " + response.getContentAsString());
                });
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

    // 테스트 실패용 member 객체 생성
    private Member createMemberForFailTest() {
        return Member.builder()
                .profileImg("fail.com")
                .build();
    }
}
