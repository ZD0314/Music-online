package com.music.online.controller;

import com.music.online.common.Result;
import com.music.online.dto.AdminLoginDTO;
import com.music.online.service.AdminService;
import com.music.online.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理员控制器
 *
 * @since 2026-03-09
 */
@Api(tags = "管理员接口")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * 管理员登录
     */
    @ApiOperation("管理员登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Validated @RequestBody AdminLoginDTO dto) {
        LoginVO loginVO = adminService.login(dto);
        return Result.success(loginVO);
    }

    /**
     * 获取统计数据
     */
    @ApiOperation("获取统计数据")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = adminService.getStatistics();
        return Result.success(statistics);
    }
}
