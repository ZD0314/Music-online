package com.music.online.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.online.common.Result;
import com.music.online.service.AlbumService;
import com.music.online.vo.AlbumVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 专辑控制器 - 用户端
 *
 * @since 2026-03-09
 */
@Api(tags = "专辑接口 - 用户端")
@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
public class UserAlbumController {

    private final AlbumService albumService;

    /**
     * 获取专辑列表
     */
    @ApiOperation("获取专辑列表")
    @GetMapping
    public Result<Page<AlbumVO>> getAlbums(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer size,
            @ApiParam("搜索关键词") @RequestParam(required = false) String keyword,
            @ApiParam("歌手ID") @RequestParam(required = false) Long artistId
    ) {
        Page<AlbumVO> albumPage = albumService.getAlbumList(page, size, keyword, artistId);
        return Result.success(albumPage);
    }

    /**
     * 获取专辑详情
     */
    @ApiOperation("获取专辑详情")
    @GetMapping("/{id}")
    public Result<AlbumVO> getAlbumById(@ApiParam("专辑ID") @PathVariable Long id) {
        AlbumVO album = albumService.getAlbumById(id);
        return Result.success(album);
    }
}
