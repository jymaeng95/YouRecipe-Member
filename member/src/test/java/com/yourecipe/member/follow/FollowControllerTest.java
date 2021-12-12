package com.yourecipe.member.follow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourecipe.member.controller.FollowController;
import com.yourecipe.member.model.Follow;
import com.yourecipe.member.service.FollowService;
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
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FollowController.class)
public class FollowControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private FollowService followService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = Jackson2ObjectMapperBuilder.json().build();
    }

    @Test
    @DisplayName("팔로우 컨트롤러 : 팔로우 추가(성공)")
    void 팔로우_추가_성공() throws Exception {
        //given
        given(followService.doFollow(any())).willReturn(true);
        Follow follow = createFollowList();
        String url = "/follow";

        //when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .post(url)
                .content(objectMapper.writeValueAsString(follow))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        String expectBody = "{\"message\":\"팔로우를 성공했습니다.\",\"status\":true}";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectBody);
    }

    @Test
    @DisplayName("팔로우 컨트롤러 : 팔로우 추가 시도(실패)")
    void 팔로우_추가_실패() throws Exception {
        // given
        given(followService.doFollow(any())).willReturn(false);
        Follow follow = createFollowList();
        String url = "/follow";

        // when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .post(url)
                .content(objectMapper.writeValueAsString(follow))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        String expectBody = "{\"message\":\"팔로우를 실패했습니다.\",\"status\":false}";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectBody);
    }

    @Test
    @DisplayName("팔로우 컨트롤러 : 팔로우 조회 시도(성공)")
    void 팔로우_조회_성공() throws Exception {
        //givne
        List<Follow> follows = createFollowLists();
        given(followService.getFollowList(anyInt())).willReturn(follows);
        String url = "/follow/2";

        //when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        String expectBody = "{\"data\":[{\"memberId\":2,\"feedId\":1}," +
                "{\"memberId\":2,\"feedId\":3}]," +
                "\"message\":\"팔로우 리스트 조회를 성공했습니다.\"," +
                "\"status\":true}";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectBody);
    }

    @Test
    @DisplayName("팔로우 컨트롤러 : 팔로우 조회 시도(실패)")
    void  팔로우_조회_실패() throws Exception {
        // given
        given(followService.getFollowList(anyInt())).willReturn(null);
        String url = "/follow/0";

        // when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
        .get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        String expectBody = "{\"data\":[],\"message\":\"팔로우 리스트가 없습니다.\",\"status\":true}";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectBody);

    }

    @Test
    @DisplayName("팔로우 컨트롤러 : 팔로우 삭제/언팔로잉 시도(성공)")
    void 팔로우_삭제_성공() throws Exception {
        // given
        given(followService.doUnfollow(anyInt(), anyInt())).willReturn(true);
        String url = "/unfollow/2/3";

        // when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
        .delete(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        // then
        String expectBody = "{\"message\":\"팔로우를 취소했습니다.\",\"status\":true}";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectBody);
    }

    @Test
    @DisplayName("팔로우 컨트롤러 : 팔로우 삭제/언팔로잉 시도(실패)")
    void 팔로우_삭제_실패() throws Exception {
        // given
        given(followService.doUnfollow(anyInt(),anyInt())).willReturn(false);
        String url = "/unfollow/2/5";

        // when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
        .delete(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        String expectBody = "{\"message\":\"팔로우 취소에 실패했습니다.\",\"status\":false}";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectBody);


    }

    @Test
    @DisplayName("팔로우 컨트롤러 : 팔로우 리스트 삭제(성공)")
    void 팔로우_리스트_삭제_성공() throws Exception {
        //given
        given(followService.clearFollowList(anyInt())).willReturn(true);
        String url = "/follow/list/2";

        // when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
        .delete(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        // then
        String expectBody = "{\"message\":\"팔로우 리스트를 삭제했습니다.\",\"status\":true}";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectBody);
    }

    @Test
    @DisplayName("팔로우 컨트롤러 : 팔로우 리스트 삭제(실패)")
    void 팔로우_리스트_삭제_실패() throws Exception {
        // given
        given(followService.clearFollowList(anyInt())).willReturn(false);
        String url = "/follow/list/0" ;

        // when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
        .delete(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        String expectBody = "{\"message\":\"팔로우 리스트 삭제에 실패했습니다.\",\"status\":false}";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectBody);
    }


    private static Follow createFollowList() {
        return Follow.builder()
                .memberId(2)
                .feedId(1)
                .build();
    }

    private static List<Follow> createFollowLists() {
        Follow follow1 = Follow.builder()
                .memberId(2)
                .feedId(1)
                .build();

        Follow follow2 = Follow.builder()
                .memberId(2)
                .feedId(3)
                .build();

        return List.of(follow1, follow2);
    }
}
