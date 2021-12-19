package com.yourecipe.member.controller;

import com.yourecipe.member.model.Member;
import com.yourecipe.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/member")
@Api(tags = "Member", value = "회원 컨트롤러")
public class MemberController {
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    private final MemberService memberService;

    @ApiOperation(value = "회원 가입", notes = "회원가입을 시도한다.")
    @PostMapping
    public ResponseEntity<Map<String, Object>> signUpMember(@RequestBody Member member) {
        logger.info("회원 가입 시도");

        Map<String, Object> map = new HashMap<>();
        try {
            if (memberService.signUpMember(member)) {
                map.put("status", true);
                map.put("message", "회원가입을 성공했습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            map.put("status", false);
            map.put("message", "회원가입을 실패했습니다.");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        catch (RuntimeException e) {
            return ServiceException(e);
        }
    }

    @ApiOperation(value = "회원 정보 조회", notes = "회원 아이디를 이용해 로그인을 시도하거나 회원 아이디를 이용해 회원정보를 가져온다.")
    @GetMapping("{memberId}")
    public ResponseEntity<Map<String, Object>> findMemberByUserId(@PathVariable("memberId") int memberId) {
        logger.info("회원 ID로 회원정보 조회 시도");

        Map<String, Object> map = new HashMap<>();
        try {
            Member member = Optional.ofNullable(memberService.findMemberById(memberId)).orElseGet(Member::new);
            map.put("status", true);
            map.put("data", member);
            if (member.getMemberId() != memberId) map.put("message", "등록된 회원정보가 없습니다.");
            else map.put("message", "회원정보를 가져왔습니다.");

            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ServiceException(e);
        }
    }

    @ApiOperation(value = "회원 정보 수정", notes = "회원 정보를 수정한다.")
    @PutMapping
    public ResponseEntity<Map<String, Object>> modifyMember(@RequestBody Member member) {
        logger.info("회원 정보 수정 시도");

        Map<String, Object> map = new HashMap<>();
        try {
            if(memberService.editMember(member)) {
                map.put("status", true);
                map.put("message", "회원정보를 수정했습니다.");

                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            map.put("status", false);
            map.put("message", "회원정보 수정에 실패했습니다.");

            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        catch (RuntimeException e) {
            return ServiceException(e);
        }
    }

    @ApiOperation(value = "회원 탈퇴", notes = "회원 이디를 이용해 회원 탈퇴를 시도한다.")
    @DeleteMapping("{memberId}")
    public ResponseEntity<Map<String, Object>> withdrwalMember(@PathVariable("memberId") int memberId) {
        logger.info("회원 탈퇴 시도");

        Map<String, Object> map = new HashMap<>();
        try {
            if(memberService.quitMember(memberId)) {
                map.put("status", true);
                map.put("message", "회원탈퇴를 성공했습니다.");

                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            map.put("status", false);
            map.put("message","회원탈퇴를 실패했습니다.");

            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        catch (RuntimeException e) {
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
