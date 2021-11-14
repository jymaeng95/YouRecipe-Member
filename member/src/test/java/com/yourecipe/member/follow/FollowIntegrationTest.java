package com.yourecipe.member.follow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourecipe.member.model.Follow;
import com.yourecipe.member.repository.FollowRepository;
import com.yourecipe.member.service.FollowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
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

        return List.of(follow1,follow2);
    }
}
