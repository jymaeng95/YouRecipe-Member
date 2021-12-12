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

import java.util.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "Follow", value = "팔로우 컨트롤러")
public class FollowController {
    private static final Logger logger = LoggerFactory.getLogger(FollowController.class);

    private final FollowService followService;

    @ApiOperation(value = "팔로우 추가", notes = "회원이 다른 회원의 레시피 피드를 확인해 팔로우를 추가한다.")
    @PostMapping("/follow")
    public ResponseEntity<Map<String, Object>> doFollow(@RequestBody Follow follow) {
        logger.info("팔로우 추가 시도");
        Map<String, Object> map = new HashMap<>();

        try {
            if (followService.doFollow(follow)) {
                map.put("status",true);
                map.put("message", "팔로우를 성공했습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            map.put("status",false);
            map.put("message","팔로우를 실패했습니다.");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        catch (RuntimeException e) {
            return ServiceException(e);
        }
    }

    @ApiOperation(value = "팔로우 해제", notes = "회원이 다른 회원의 레시피 피드를 언팔로우 한다.")
    @DeleteMapping("unfollow/{memberId}/{feedId}")
    public ResponseEntity<Map<String, Object>> doUnfollow(@PathVariable("memberId") int memberId, @PathVariable("feedId") int feedId) {
        logger.info("팔로우 해제 시도");
        Map<String, Object> map = new HashMap<>();

        try {
            if (followService.doUnfollow(memberId, feedId)) {
                map.put("status", true);
                map.put("message", "팔로우를 취소했습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            map.put("status", false);
            map.put("message", "팔로우 취소에 실패했습니다.");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        catch (RuntimeException e) {
            return ServiceException(e);
        }
    }

    @ApiOperation(value = "팔로우 리스트 삭제", notes = "회원이 탈퇴하는 경우 팔로우 리스트를 전체 삭제한다.")
    @DeleteMapping("/follow/list/{memberId}")
    public ResponseEntity<Map<String, Object>> clearFollowList(@PathVariable("memberId") int memberId) {
        logger.info("팔로우 리스트 삭제 시도");
        Map<String, Object> map = new HashMap<>();

        try {
            if (followService.clearFollowList(memberId)) {
                map.put("status",true);
                map.put("message", "팔로우 리스트를 삭제했습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            map.put("status",false);
            map.put("message", "팔로우 리스트 삭제에 실패했습니다.");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        catch (RuntimeException e) {
            return ServiceException(e);
        }
    }

    // 팔로우 리스트 조회
    @ApiOperation(value = "팔로우 리스트 조회", notes = "회원이 자신의 팔로잉 리스트를 확인할 수 있다.")
    @GetMapping("follow/{memberId}")
    public ResponseEntity<Map<String, Object>> getFollowList(@PathVariable("memberId") int memberId) {
        logger.info("팔로우 리스트 조회 시도");
        Map<String, Object> map = new HashMap<>();

        try {
            List<Follow> follow = Optional.ofNullable(followService.getFollowList(memberId)).orElseGet(ArrayList::new);
            map.put("status", true);
            map.put("data", follow);

            if(follow.size() > 0) map.put("message", "팔로우 리스트 조회를 성공했습니다.");
            else map.put("message","팔로우 리스트가 없습니다.");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ServiceException(e);
        }
    }

    /* 서비스 로직 호출 실패 시 예외 처리 */
    private ResponseEntity<Map<String, Object>> ServiceException(Exception e) {
        Map<String, Object> exceptionMap = new HashMap<>();
        exceptionMap.put("status", false);
        exceptionMap.put("message", e.getStackTrace());
        return new ResponseEntity<>(exceptionMap, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
