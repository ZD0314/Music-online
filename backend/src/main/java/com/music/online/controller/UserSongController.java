package com.music.online.controller;

import com.music.online.common.Result;
import com.music.online.service.SongService;
import com.music.online.vo.SongVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 歌曲控制器 - 用户端
 *
 * @since 2026-03-09
 */
@Api(tags = "歌曲接口 - 用户端")
@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
public class UserSongController {

    private final SongService songService;

    /**
     * 获取歌曲详情
     */
    @ApiOperation("获取歌曲详情")
    @GetMapping("/{id}")
    public Result<SongVO> getSongById(@ApiParam("歌曲ID") @PathVariable Long id) {
        SongVO song = songService.getSongById(id);
        return Result.success(song);
    }

    /**
     * 获取歌曲歌词
     */
    @ApiOperation("获取歌曲歌词")
    @GetMapping("/{id}/lyric")
    public Result<String> getSongLyric(@ApiParam("歌曲ID") @PathVariable Long id) {
        SongVO song = songService.getSongById(id);
        return Result.success(song.getLyricUrl());
    }

    /**
     * 增加播放次数
     */
    @ApiOperation("增加播放次数")
    @PostMapping("/{id}/play")
    public Result<Void> incrementPlayCount(@ApiParam("歌曲ID") @PathVariable Long id) {
        songService.incrementPlayCount(id);
        return Result.success();
    }

    /**
     * 根据歌单ID获取歌曲列表
     */
    @ApiOperation("根据歌单ID获取歌曲列表")
    @GetMapping("/playlist/{playlistId}")
    public Result<List<SongVO>> getSongsByPlaylistId(@ApiParam("歌单ID") @PathVariable Long playlistId) {
        List<SongVO> songs = songService.getSongsByPlaylistId(playlistId);
        return Result.success(songs);
    }

    /**
     * 根据歌手ID获取热门歌曲
     */
    @ApiOperation("根据歌手ID获取热门歌曲")
    @GetMapping("/artist/{artistId}/hot")
    public Result<List<SongVO>> getHotSongsByArtistId(
            @ApiParam("歌手ID") @PathVariable Long artistId,
            @ApiParam("数量限制") @RequestParam(defaultValue = "10") Integer limit
    ) {
        List<SongVO> songs = songService.getHotSongsByArtistId(artistId, limit);
        return Result.success(songs);
    }

    /**
     * 根据专辑ID获取歌曲列表
     */
    @ApiOperation("根据专辑ID获取歌曲列表")
    @GetMapping("/album/{albumId}")
    public Result<List<SongVO>> getSongsByAlbumId(@ApiParam("专辑ID") @PathVariable Long albumId) {
        List<SongVO> songs = songService.getSongsByAlbumId(albumId);
        return Result.success(songs);
    }
}
