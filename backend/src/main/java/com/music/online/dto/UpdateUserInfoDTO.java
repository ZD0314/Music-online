package com.music.online.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

/**
 * 更新用户信息DTO
 */
@Data
@ApiModel("更新用户信息DTO")
public class UpdateUserInfoDTO {

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("性别 (0-未知, 1-男, 2-女)")
    private Integer gender;

    @ApiModelProperty("生日 (格式: YYYY-MM-DD)")
    private String birthday;

    @ApiModelProperty("手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @ApiModelProperty("邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty("头像URL")
    private String avatar;

    @ApiModelProperty("个人简介")
    private String bio;
}
