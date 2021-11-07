package com.yourecipe.member.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ApiModel(description = "회원의 레시피 찜 정보를 위한 도메인 객체")
@Getter
@ToString
@Builder
public class Scrap {
    @ApiModelProperty(value = "회원 ID")
    private int memberId;

    @ApiModelProperty(value = "레시피 ID")
    private int recipeId;
}
