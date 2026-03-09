package com.music.online.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.online.common.Result;
import com.music.online.dto.PlaylistDTO;
import com.music.online.service.PlaylistService;
import com.music.online.vo.PlaylistVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 歌单管理接口
 *
 * @since 2026-03-09
 */
@Api(tags = "歌单管理")
@RestController
@RequestMapping("/admin/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    /**
     * 分页查询歌单列表
     */
    @ApiOperation("分页查询歌单列表")
    @GetMapping
    public Result<Page<PlaylistVO>> getPlaylistList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String name) {
        Page<PlaylistVO> result = playlistService.getPlaylistList(page, size, name);
        return Result.success(result);
    }

    /**
     * 获取歌单详情
     */
    @ApiOperation("获取歌单详情")
    @GetMapping("/{id}")
    public Result<PlaylistVO> getPlaylistById(@PathVariable Long id) {
        PlaylistVO result = playlistService.getPlaylistById(id);
        return Result.success(result);
    }

    /**
     * 创建歌单
     */
    @ApiOperation("创建歌单")
    @PostMapping
    public Result<Long> createPlaylist(@Validated @RequestBody PlaylistDTO dto) {
        Long id = playlistService.createPlaylist(dto);
        return Result.success(id);
    }

    /**
     * 更新歌单
     */
    @ApiOperation("更新歌单")
    @PutMapping("/{id}")
    public Result<Void> updatePlaylist(@PathVariable Long id, @Validated @RequestBody PlaylistDTO dto) {
        playlistService.updatePlaylist(id, dto);
        return Result.success();
    }

    /**
     * 删除歌单
     */
    @ApiOperation("删除歌单")
    @DeleteMapping("/{id}")
    public Result<Void> deletePlaylist(@PathVariable Long id) {
        playlistService.deletePlaylist(id);
        return Result.success();
    }

    /**
     * 添加歌曲到歌单
     */
    @ApiOperation("添加歌曲到歌单")
    @PostMapping("/{id}/songs")
    public Result<Void> addSongToPlaylist(@PathVariable Long id, @RequestParam Long songId) {
        playlistService.addSongToPlaylist(id, songId);
        return Result.success();
    }

    /**
     * 从歌单移除歌曲
     */
    @ApiOperation("从歌单移除歌曲")
    @DeleteMapping("/{playlistId}/songs/{songId}")
    public Result<Void> removeSongFromPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        playlistService.removeSongFromPlaylist(playlistId, songId);
        return Result.success();
    }
}
