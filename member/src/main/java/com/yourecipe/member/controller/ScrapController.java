package com.yourecipe.member.controller;

import com.yourecipe.member.model.Scrap;
import com.yourecipe.member.service.ScrapService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/scrap")
@Api(tags = "Scrap", value = "스크랩 컨트롤러")
public class ScrapController {
    private static final Logger logger = LoggerFactory.getLogger(ScrapController.class);

    private final ScrapService scrapService;

    @ApiOperation(value = "스크랩 추가", notes = "관심이 있는 레시피를 스크랩해서 추가한다.")
    @PostMapping
    public ResponseEntity<String> doScarp(@RequestBody Scrap scrap) {
        logger.info("레시피 스크랩 추가 시도");
        if (scrapService.doScrap(scrap)) return new ResponseEntity<>("스크랩 추가 성공", HttpStatus.OK);
        return new ResponseEntity<>("스크랩 추가 실패", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "스크랩 취소", notes = "관심있는 레시피를 스크랩 목록에서 삭제한다.")
    @DeleteMapping("/{recipeId}")
    public ResponseEntity<String> cancelScrap(@PathVariable("recipeId") int recipeId) {
        logger.info("관심있는 레시피 스크랩 취소 시도");
        if (scrapService.cancelScrap(recipeId)) return new ResponseEntity<>("스크랩 취소 성공", HttpStatus.OK);
        return new ResponseEntity<>("스크랩 취소 실패",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "스크랩 리스트 삭제", notes = "회원의 스크랩 목록을 전체 삭제한다. (회원 탈퇴, 목록 초기화 경우)")
    @DeleteMapping("/list/{memberId}")
    public ResponseEntity<String> clearScrapList(@PathVariable("memberId") int memberId) {
        logger.info("스크랩 리스트 전체 삭제 시도");
        if(scrapService.clearScrapList(memberId)) return new ResponseEntity<>("스크랩 리스트 삭제 성공",HttpStatus.OK);
        return new ResponseEntity<>("스크랩 리스트 삭제 실패", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "스크랩 리스트 조회", notes = "회원의 스크랩 목록을 가져온다.")
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getScrapList(@PathVariable("memberId") int memberId) {
        logger.info("스크랩 리스트 조회 시도");

        Optional<List<Scrap>> scrapList = scrapService.getScrapList(memberId);
        if(scrapList.isPresent())
            return new ResponseEntity<>(scrapList, HttpStatus.OK);
        return new ResponseEntity<>("스크랩 리스트 조회 실패", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
