package com.music.online.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息VO
 */
@Data
@ApiModel("用户信息VO")
public class UserVO {

    @ApiModelProperty("用户ID")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("性别 (0-未知, 1-男, 2-女)")
    private Integer gender;

    @ApiModelProperty("生日")
    private String birthday;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("头像URL")
    private String avatar;

    @ApiModelProperty("个人简介")
    private String bio;

    @ApiModelProperty("状态 (0-禁用, 1-正常)")
    private Integer status;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
