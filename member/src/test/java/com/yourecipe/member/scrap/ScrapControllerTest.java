package com.yourecipe.member.scrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourecipe.member.controller.ScrapController;
import com.yourecipe.member.model.Scrap;
import com.yourecipe.member.service.ScrapService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ScrapController.class)
public class ScrapControllerTest {
    @MockBean
    private ScrapService scrapService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = Jackson2ObjectMapperBuilder.json().build();
    }

    @Test
    @DisplayName("스크랩 컨트롤러 : 스크랩 추가(성공)")
    void 스크랩_추가_성공() throws Exception {
        //given
        given(scrapService.doScrap(any())).willReturn(true);
        Scrap scrap = createScrapForTest();
        String url = "/scrap";

        //when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .post(url)
                .content(objectMapper.writeValueAsString(scrap))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("스크랩 추가 성공");
    }

    @Test
    @DisplayName("스크랩 컨트롤러 : 스크랩 추가(실패)")
    void 스크랩_추가_실패() throws Exception {
        //given
        given(scrapService.doScrap(any())).willReturn(false);
        Scrap scrap = createScrapForTest();
        String url = "/scrap";

        //when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .post(url)
                .content(objectMapper.writeValueAsString(scrap))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo("스크랩 추가 실패");
    }

    @Test
    @DisplayName("스크랩 컨트롤러 :  스크랩 취소(성공)")
    void 스크랩_취소_성공() throws Exception {
        //given
        given(scrapService.cancelScrap(anyInt())).willReturn(true);
        String url = "/scrap/1";

        //when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .delete(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("스크랩 취소 성공");
    }

    @Test
    @DisplayName("스크랩 컨트롤러 : 스크랩 취소(실패)")
    void 스크랩_취소_실패() throws Exception {
        //given
        given(scrapService.cancelScrap(anyInt())).willReturn(false);
        String url = "/scrap/0";

        //when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .delete(url))
                .andExpect(status().isInternalServerError())
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo("스크랩 취소 실패");
    }

    @Test
    @DisplayName("스크랩 컨트롤러 : 스크랩 리스트 삭제(성공)")
    void  스크랩_리스트_삭제_성공() throws Exception {
        //given
        given(scrapService.clearScrapList(anyInt())).willReturn(true);
        String url = "/scrap/list/1";

        //when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
        .delete(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("스크랩 리스트 삭제 성공");
    }

    @Test
    @DisplayName("스크랩 컨트롤러 : 스크랩 리스트 삭제(실패)")
    void 스크랩_리스트_삭제_실패() throws Exception {
        //given
        given(scrapService.clearScrapList(anyInt())).willReturn(false);
        String url = "/scrap/list/0";

        //when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
        .delete(url)
        .param("memberId", String.valueOf(0)))
                .andExpect(status().isInternalServerError())
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo("스크랩 리스트 삭제 실패");
    }

    @Test
    @DisplayName("스크랩 컨트롤러 : 스크랩 리스트 조회(성공)")
    void 스크랩_리스트_조회_성공() throws Exception {
        //given
        List<Scrap> scraps = createScrapListForTest();
        given(scrapService.getScrapList(anyInt())).willReturn(Optional.of(scraps));
        String url = "/scrap/1";

        //when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
        .get(url)
        .param("memberId",String.valueOf(1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[{\"memberId\":1,\"recipeId\":1},{\"memberId\":1,\"recipeId\":2}]");
    }

    @Test
    @DisplayName("스크랩 컨트롤러 : 스크랩 리스트 조회(실패)")
    void 스크랩_리스트_조회_실패() throws Exception {
        //given
        given(scrapService.getScrapList(anyInt())).willReturn(Optional.empty());
        String url = "/scrap/0";

        //when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
        .get(url))
//        .param("memberId",String.valueOf(0)))
                .andExpect(status().isInternalServerError())
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getContentAsString()).isEqualTo("스크랩 리스트 조회 실패");
    }

    private static Scrap createScrapForTest() {
        return Scrap.builder()
                .memberId(1)
                .recipeId(1)
                .build();
    }

    private static List<Scrap> createScrapListForTest() {
        Scrap scrap1 = Scrap.builder()
                .memberId(1)
                .recipeId(1)
                .build();
        Scrap scrap2 = Scrap.builder()
                .memberId(1)
                .recipeId(2)
                .build();

        return Arrays.asList(scrap1, scrap2);
    }

}
