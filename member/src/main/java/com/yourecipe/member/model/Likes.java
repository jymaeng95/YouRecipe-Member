package com.yourecipe.member.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ApiModel(description = "회원의 관심 카테고리 정보를 위한 도메인 객체")
@Getter
@ToString
@Builder
public class Likes {
    @ApiModelProperty(value = "회원 ID")
    private int memberId;

    @ApiModelProperty(value = "카테고리 ID")
    private int categoryId;
}
