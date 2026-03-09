package com.music.online.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录请求
 *
 * @since 2026-03-09
 */
@Data
@ApiModel("用户登录请求")
public class UserLoginDTO {

    @ApiModelProperty(value = "账号(用户名/手机号/邮箱)", required = true)
    @NotBlank(message = "账号不能为空")
    private String account;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;
}
