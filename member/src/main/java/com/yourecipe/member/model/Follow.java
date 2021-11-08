package com.yourecipe.member.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ApiModel(description = "회원의 팔로잉 정보를 위한 도메인 객체")
@Getter
@ToString
@Builder
public class Follow {
    @ApiModelProperty(value = "회원 ID")
    private int memberId;

    @ApiModelProperty(value = "피드 ID")
    private int feedId;
}
