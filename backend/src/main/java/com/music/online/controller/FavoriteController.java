package com.music.online.controller;

import com.music.online.common.Result;
import com.music.online.dto.FavoriteDTO;
import com.music.online.service.FavoriteService;
import com.music.online.vo.FavoriteVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 收藏控制器 - 用户端
 *
 * @since 2026-03-09
 */
@Api(tags = "收藏接口 - 用户端")
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    /**
     * 添加收藏
     */
    @ApiOperation("添加收藏")
    @PostMapping
    public Result<Void> addFavorite(
            HttpServletRequest request,
            @Validated @RequestBody FavoriteDTO dto
    ) {
        Long userId = (Long) request.getAttribute("userId");
        favoriteService.addFavorite(userId, dto);
        return Result.success();
    }

    /**
     * 取消收藏
     */
    @ApiOperation("取消收藏")
    @DeleteMapping("/{id}")
    public Result<Void> removeFavorite(
            HttpServletRequest request,
            @ApiParam("收藏ID") @PathVariable Long id
    ) {
        Long userId = (Long) request.getAttribute("userId");
        favoriteService.removeFavorite(userId, id);
        return Result.success();
    }

    /**
     * 获取收藏列表
     */
    @ApiOperation("获取收藏列表")
    @GetMapping
    public Result<List<FavoriteVO>> getFavoriteList(
            HttpServletRequest request,
            @ApiParam("收藏类型: 1-歌曲 2-专辑 3-歌单") @RequestParam(required = false) Integer type
    ) {
        Long userId = (Long) request.getAttribute("userId");
        List<FavoriteVO> list = favoriteService.getFavoriteList(userId, type);
        return Result.success(list);
    }

    /**
     * 检查是否已收藏
     */
    @ApiOperation("检查是否已收藏")
    @GetMapping("/check")
    public Result<Boolean> checkFavorite(
            HttpServletRequest request,
            @ApiParam("收藏类型") @RequestParam Integer type,
            @ApiParam("目标ID") @RequestParam Long targetId
    ) {
        Long userId = (Long) request.getAttribute("userId");
        boolean isFavorited = favoriteService.isFavorited(userId, type, targetId);
        return Result.success(isFavorited);
    }
}
