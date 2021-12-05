package com.yourecipe.member.scrap;

import com.yourecipe.member.model.Scrap;
import com.yourecipe.member.repository.ScrapRepository;
import com.yourecipe.member.service.ScrapService;
import com.yourecipe.member.service.ScrapServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ScrapServiceTest {
    @Mock
    private ScrapRepository scrapRepository;

    @InjectMocks
    private ScrapServiceImpl scrapService;

    @Test
    @DisplayName("스크랩 서비스 : 스크랩 등록(성공)")
    void 스크랩_등록_성공() {
        // given
        given(scrapRepository.addScrap(any())).willReturn(1);
        Scrap scrap = createScrapForTest();

        // when
        boolean result = scrapService.doScrap(scrap);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("스크랩 서비스 : 스크랩 등록(실패)")
    void 스크랩_등록_실패() {
        // given
        given(scrapRepository.addScrap(any())).willReturn(0);
        Scrap scrap = createScrapForTest();

        // when
        boolean result = scrapService.doScrap(scrap);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("스크랩 서비스 : 스크랩 해제(성공)")
    void 스크랩_해제_성공() {
        // given
        given(scrapRepository.deleteScrap(anyInt())).willReturn(1);

        //when
        boolean result = scrapService.cancelScrap(1);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("스크랩 서비스 : 스크랩 해제(실패)")
    void 스크랩_해제_실패() {
        // given
        given(scrapRepository.deleteScrap(anyInt())).willReturn(0);

        //when
        boolean result = scrapService.cancelScrap(0);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("스크랩 서비스 : 스크랩 리스트 삭제(성공)")
    void 스크랩_리스트_삭제_성공() {
        //given
        given(scrapRepository.deleteAllScrap(anyInt())).willReturn(1);

        //when
        boolean result = scrapService.clearScrapList(1);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("스크랩 서비스 : 스크랩 리스트 삭제(실패)")
    void 스크랩_리스트_삭제_실패() {
        //given
        given(scrapRepository.deleteAllScrap(anyInt())).willReturn(0);

        //when
        boolean result = scrapService.clearScrapList(0);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("스크랩 서비스 : 스크랩 리스트 조획(성공)")
    void 스크랩_리스트_조회_성공() {
        // given
        List<Scrap> scraps = createScrapListForTest();
        given(scrapRepository.selectScrapList(anyInt())).willReturn(scraps);

        //when
        Optional<List<Scrap>> scrapList = scrapService.getScrapList(1);

        //then
        if(scrapList.isPresent()) {
            assertThat(scrapList.get().get(0).getMemberId()).isEqualTo(1);
            assertThat(scrapList.get().get(0).getRecipeId()).isEqualTo(1);
            assertThat(scrapList.get().get(1).getMemberId()).isEqualTo(1);
            assertThat(scrapList.get().get(1).getRecipeId()).isEqualTo(2);
        }
    }

    @Test
    @DisplayName("스크랩 서비스 : 스크랩 리스트 조회(실패)")
    void 스크랩_리스트_조회_실패() {
        //given
        given(scrapRepository.selectScrapList(anyInt())).willReturn(null);

        //when
        Optional<List<Scrap>> scrapList = scrapService.getScrapList(1);

        //then
        assertThat(scrapList.isPresent()).isFalse();
        assertThat(scrapList.isEmpty()).isTrue();
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
