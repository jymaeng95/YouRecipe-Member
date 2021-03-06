package com.yourecipe.member.follow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourecipe.member.model.Follow;
import com.yourecipe.member.repository.FollowRepository;
import com.yourecipe.member.service.FollowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
public class FollowIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private FollowService followService;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.objectMapper = Jackson2ObjectMapperBuilder.json().build();
    }

    /*
     * 테스트 flow : Controller -> Service -> Repository
     */

    @Test
    @DisplayName("팔로우 통합 테스트 : 팔로우 시도(성공)")
    void 팔로우_시도_성공() throws Exception {
        String url = "/follow";
        Follow follow = createFollowList();

        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .post(url)
                .content(objectMapper.writeValueAsString(follow))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        String expectBody = "{\"message\":\"팔로우를 성공했습니다.\",\"status\":true}";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectBody);

        boolean result = followService.doFollow(follow);
        assertThat(result).isTrue();
    }

    /*
        // null 값 프론트/백엔드 둘다 예외처리 필요
        @Test
        @DisplayName("팔로우 통합 테스트 : 팔로우 시도(실패)")
        void 팔로우_시도_실패() throws Exception {
            String url = "/follow";

            // 프론트 단에서 널값 체킹해서 넘기기 / 예외처리 로직 필요할듯
            Follow follow = Follow.builder().build(); // null

            MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
            .post(url)
            .content(objectMapper.writeValueAsString(follow))
            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andDo(print())
                    .andReturn().getResponse();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

            boolean result = followService.doFollow(null);
            assertThat(result).isFalse();
        }
    */
    @Test
    @DisplayName("팔로우 통합 테스트 : 팔로우 조회 시도(성공)")
    void 팔로우_조회_성공() throws Exception {
        // given
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

        // service when
        Optional<List<Follow>> follows = Optional.ofNullable(followService.getFollowList(2));

        //then
        if (follows.isPresent()) {
            assertThat(follows.get().get(0).getMemberId()).isEqualTo(2);
            assertThat(follows.get().get(0).getFeedId()).isEqualTo(1);

            assertThat(follows.get().get(1).getMemberId()).isEqualTo(2);
            assertThat(follows.get().get(1).getFeedId()).isEqualTo(3);
        }
    }

    @Test
    @DisplayName("팔로우 통합 테스트 : 팔로우 조회 시도(실패)")
    void 팔로우_조회_실패() throws Exception {
        //given
        String url = "/follow/0";

        //when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        String expectBody = "{\"data\":[],\"message\":\"팔로우 리스트가 없습니다.\",\"status\":true}";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectBody);

        List<Follow> follow = Optional.ofNullable(followService.getFollowList(0)).orElseGet(ArrayList::new);

        //then
        assertThat(follow).isEqualTo(Collections.emptyList());
        assertThat(follow.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("팔로우 통합 테스트 : 언팔로우 시도(성공)")
    void 언팔로우_성공() throws Exception {
        //given
        String url = "/unfollow/2/1";

        //when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .delete(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        String expectBody = "{\"message\":\"팔로우를 취소했습니다.\",\"status\":true}";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectBody);

    }

    @Test
    @DisplayName("팔로우 통합 테스트 : 언팔로우 시도(실패)")
    void 언팔로우_시도() throws Exception {
        //given
        String url = "/unfollow/2/5";

        //when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .delete(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        String expectBody = "{\"message\":\"팔로우 취소에 실패했습니다.\",\"status\":false}";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectBody);

        //service logic
        boolean result = followService.doUnfollow(2, 5);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("팔로우 통합 테스트 : 팔로우 리스트 삭제(성공)")
    void 팔로우_리스트_삭제_성공() throws Exception {
        //given
        String url = "/follow/list/2";

        //when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .delete(url)).andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        String expectBody = "{\"message\":\"팔로우 리스트를 삭제했습니다.\",\"status\":true}";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectBody);
    }

    @Test
    @DisplayName("팔로우 통합 테스트 : 팔로우 리스트 삭제(실패)")
    void 팔로우_리스트_삭제_실패() throws Exception {
        //given
        String url = "/follow/list/0";

        //when
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .delete(url)).andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse();

        //then
        String expectBody = "{\"message\":\"팔로우 리스트 삭제에 실패했습니다.\",\"status\":false}";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectBody);

        //service logic
        boolean result = followService.clearFollowList(0);

        assertThat(result).isFalse();
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
