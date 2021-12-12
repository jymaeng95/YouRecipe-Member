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

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/member")
@Api(tags = "Member", value = "회원 컨트롤러")
public class MemberController {
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    private final MemberService memberService;

    @ApiOperation(value = "회원 가입")
    @PostMapping
    public ResponseEntity<String> signUpMember(@RequestBody Member member) {
        logger.info("회원 가입 시도");
        if (memberService.signUpMember(member)) return new ResponseEntity<>("가입 성공", HttpStatus.OK);
        return new ResponseEntity<>("가입 실패", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "회원 정보 조회")
    @GetMapping("{memberId}")
    public ResponseEntity<?> findMemberByUserId(@PathVariable("memberId") int memberId) {
        logger.info("회원 ID로 회원정보 조회 시도");
//        Member member = memberService.findMemberById(memberId);

        Optional<Member> member = Optional.ofNullable(memberService.findMemberById(memberId));
        if (member.isPresent()) return new ResponseEntity<>(member.get(), HttpStatus.OK);
        return new ResponseEntity<>("회원정보 조회 실패", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "회원 정보 수정")
    @PutMapping
    public ResponseEntity<String> modifyMember(@RequestBody Member member) {
        logger.info("회원 정보 수정 시도");
        if (memberService.editMember(member)) return new ResponseEntity<>("수정 성공", HttpStatus.OK);
        return new ResponseEntity<>("수정 실패", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiOperation(value = "회원 탈퇴")
    @DeleteMapping("{memberId}")
    public ResponseEntity<String> withdrwalMember(@PathVariable("memberId") int memberId) {
        logger.debug("회원 탈퇴 시도");
        if (memberService.quitMember(memberId)) return new ResponseEntity<>("탈퇴 성공", HttpStatus.OK);
        return new ResponseEntity<>("탈퇴 실패", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
