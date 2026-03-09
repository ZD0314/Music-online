package com.music.online.controller;

import com.music.online.common.Result;
import com.music.online.dto.UserLoginDTO;
import com.music.online.dto.UserRegisterDTO;
import com.music.online.service.UserService;
import com.music.online.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 * @since 2026-03-09
 */
@Api(tags = "用户接口")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 用户注册
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result<LoginVO> register(@Validated @RequestBody UserRegisterDTO dto) {
        LoginVO loginVO = userService.register(dto);
        return Result.success(loginVO);
    }

    /**
     * 用户登录
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Validated @RequestBody UserLoginDTO dto) {
        LoginVO loginVO = userService.login(dto);
        return Result.success(loginVO);
    }
}
