package com.music.online.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应
 *
 * @author Claude
 * @since 2026-03-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("登录响应")
public class LoginVO {

    @ApiModelProperty("Token")
    private String token;

    @ApiModelProperty("用户信息")
    private Object userInfo;
}
