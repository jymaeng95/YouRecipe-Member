package com.yourecipe.member.controller;

import com.yourecipe.member.model.Follow;
import com.yourecipe.member.service.FollowService;
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
@RequestMapping(value = "/follow")
@Api(tags = "Follow", value = "팔로우 컨트롤러")
public class FollowController {
    private static final Logger logger = LoggerFactory.getLogger(FollowController.class);

    private final FollowService followService;

    @ApiOperation(value = "팔로우 추가", notes = "회원이 다른 회원의 레시피 피드를 확인해 팔로우를 추가한다.")
    @PostMapping
    public ResponseEntity<String> doFollow(@RequestBody Follow follow) {
        logger.info("팔로우 추가 시도");
        if(followService.doFollow(follow)) return new ResponseEntity<>("팔로잉 성공", HttpStatus.OK);
        return new ResponseEntity<>("팔로잉 실패", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "팔로우 해제", notes = "회원이 다른 회원의 레시피 피드를 언팔로우 한다.")
    @DeleteMapping("/{feedId}")
    public ResponseEntity<String> doUnfollow(@PathVariable("feedId") int feedId) {
        logger.info("팔로우 해제 시도");
        if(followService.doUnfollow(feedId)) return new ResponseEntity<>("언팔로우 성공", HttpStatus.OK);
        return new ResponseEntity<>("언팔로잉 실패", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "팔로우 리스트 삭제", notes = "회원이 탈퇴하는 경우 팔로우 리스트를 전체 삭제한다.")
    @DeleteMapping("/list/{memberId}")
    public ResponseEntity<String> clearFollowList(@PathVariable("memberId") int memberId) {
        logger.info("팔로우 리스트 삭제 시도");
        if(followService.clearFollowList(memberId)) return new ResponseEntity<>("팔로우 리스트 삭제 성공", HttpStatus.OK);
        return new ResponseEntity<>("팔로우 리스트 삭제 실패", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    // 팔로우 리스트 조회
    @ApiOperation(value = "팔로우 리스트 조회", notes = "회원이 자신의 팔로잉 리스트를 확인할 수 있다.")
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getFollowList(@PathVariable("memberId") int memberId) {
        logger.info("팔로우 리스트 조회 시도");

        Optional<List<Follow>> follow = Optional.ofNullable(followService.getFollowList(memberId));

        if(follow.isPresent()) return new ResponseEntity<>(follow, HttpStatus.OK);
        return new ResponseEntity<>("팔로우 리스트 조회 실패", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
