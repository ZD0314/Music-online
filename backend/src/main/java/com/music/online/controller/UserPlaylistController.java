package com.music.online.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.online.common.Result;
import com.music.online.service.PlaylistService;
import com.music.online.vo.PlaylistVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 歌单控制器 - 用户端
 *
 * @since 2026-03-09
 */
@Api(tags = "歌单接口 - 用户端")
@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class UserPlaylistController {

    private final PlaylistService playlistService;

    /**
     * 获取歌单列表
     */
    @ApiOperation("获取歌单列表")
    @GetMapping
    public Result<Page<PlaylistVO>> getPlaylists(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer size,
            @ApiParam("搜索关键词") @RequestParam(required = false) String keyword
    ) {
        Page<PlaylistVO> playlistPage = playlistService.getPlaylistList(page, size, keyword);
        return Result.success(playlistPage);
    }

    /**
     * 获取歌单详情
     */
    @ApiOperation("获取歌单详情")
    @GetMapping("/{id}")
    public Result<PlaylistVO> getPlaylistById(@ApiParam("歌单ID") @PathVariable Long id) {
        PlaylistVO playlist = playlistService.getPlaylistById(id);
        return Result.success(playlist);
    }
}
