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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

        //when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .post("/member")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        String expectBody = "{\"message\":\"회원가입을 성공했습니다.\",\"status\":true}";

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectBody);
    }

    @Test
    @DisplayName("회원 컨트롤러 : 회원 가입 시도(실패)")
    void 회원가입_실패() throws Exception {
        //given
        Member member = createMemberForFailTest();
        given(memberService.signUpMember(any())).willReturn(false);

        String content = objectMapper.writeValueAsString(member);

        //when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .post("/member")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        String expectBody = "{\"message\":\"회원가입을 실패했습니다.\",\"status\":false}";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectBody);
    }

    @Test
    @DisplayName("회원 컨트롤러 : 회원 정보 조회(성공)")
    void 회원조회_성공() throws Exception {
        //given
        String url = "/member/1";
        Member member = createMemberForTest();
        given(memberService.findMemberById(anyInt())).willReturn(member);

        String content = objectMapper.writeValueAsString(member);

        //when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .get(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        String expectBody = "{\"data\":{\"memberId\":1,\"email\":\"zayson.maeng@gmail.com\",\"nickname\":\"zayson\",\"profileImg\":\"test.com\"}," +
                "\"message\":\"회원정보를 가져왔습니다.\",\"status\":true}";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectBody);
    }

    @Test
    @DisplayName("회원 컨트롤러 : 회원 정보 조회(실패)")
    void 회원조회_실패() throws Exception {
        //given
        String url = "/member/3";
        given(memberService.findMemberById(anyInt())).willReturn(null);

        //when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        String expectBody = "{\"data\":{\"memberId\":0,\"email\":null,\"nickname\":null,\"profileImg\":null}," +
                "\"message\":\"등록된 회원정보가 없습니다.\",\"status\":true}";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectBody);

    }

    @Test
    @DisplayName("회원 컨트롤러 : 회원 정보 수정(성공)")
    void 회원수정_성공() throws Exception {
        //given
        String url = "/member";
        Member member = createMemberForTest();
        given(memberService.editMember(any())).willReturn(true);

        String content = objectMapper.writeValueAsString(member);

        //when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .put(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        String expectBody = "{\"message\":\"회원정보를 수정했습니다.\",\"status\":true}";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectBody);
    }

    @Test
    @DisplayName("회원 컨트롤러 : 회원 정보 수정(실패)")
    void 회원수정_실패() throws Exception {
        //given
        String url = "/member";
        Member member = createMemberForFailTest();
        given(memberService.editMember(any())).willReturn(false);

        String content = objectMapper.writeValueAsString(member);

        //when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .put(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        String expectBody = "{\"message\":\"회원정보 수정에 실패했습니다.\",\"status\":false}";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectBody);
    }

    @Test
    @DisplayName("회원 컨트롤러 : 회원 정보 삭제(성공)")
    void 회원삭제_성공() throws Exception {
        //given
        String url = "/member/1";
        Member member = createMemberForTest();
        given(memberService.quitMember(anyInt())).willReturn(true);

        String content = objectMapper.writeValueAsString(member);

        //then
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .delete(url)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        String expectBody = "{\"message\":\"회원탈퇴를 성공했습니다.\",\"status\":true}";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectBody);

    }

    @Test
    @DisplayName("회원 컨트롤러 : 회원 정보 삭제(실패)")
    void 회원삭제_실패() throws Exception {
        //given
        String url = "/member/0";
        given(memberService.quitMember(anyInt())).willReturn(false);

        //whem, then
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .delete(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        String expectBody = "{\"message\":\"회원탈퇴를 실패했습니다.\",\"status\":false}";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectBody);

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
