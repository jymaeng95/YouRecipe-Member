package com.yourecipe.member.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel(description = "회원 정보를 위한 도메인 객체")
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @ApiModelProperty(value = "회원 ID")
    private int memberId;

    @ApiModelProperty(value = "회원 Email")
    private String email;

    @ApiModelProperty(value = "회원 닉네임")
    private String nickname;

    @ApiModelProperty(value = "프로필 이미지 경로")
    /* 타입 URL로 ? */
    private String profileImg;
}
