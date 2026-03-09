package com.music.online.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.online.common.Result;
import com.music.online.dto.SongDTO;
import com.music.online.entity.Song;
import com.music.online.service.SongService;
import com.music.online.vo.SongVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 歌曲控制器
 *
 * @since 2026-03-09
 */
@Api(tags = "歌曲管理接口")
@RestController
@RequestMapping("/admin/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    /**
     * 分页查询歌曲列表
     */
    @ApiOperation("分页查询歌曲列表")
    @GetMapping
    public Result<Page<SongVO>> getSongList(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer size,
            @ApiParam("歌曲名称") @RequestParam(required = false) String name,
            @ApiParam("歌手ID") @RequestParam(required = false) Long artistId,
            @ApiParam("专辑ID") @RequestParam(required = false) Long albumId
    ) {
        Page<SongVO> songPage = songService.getSongList(page, size, name, artistId, albumId);
        return Result.success(songPage);
    }

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
     * 创建歌曲
     */
    @ApiOperation("创建歌曲")
    @PostMapping
    public Result<Void> createSong(@Valid @RequestBody SongDTO songDTO) {
        Song song = new Song();
        BeanUtils.copyProperties(songDTO, song);
        songService.createSong(song);
        return Result.success();
    }

    /**
     * 更新歌曲
     */
    @ApiOperation("更新歌曲")
    @PutMapping("/{id}")
    public Result<Void> updateSong(
            @ApiParam("歌曲ID") @PathVariable Long id,
            @Valid @RequestBody SongDTO songDTO
    ) {
        Song song = new Song();
        BeanUtils.copyProperties(songDTO, song);
        song.setId(id);
        songService.updateSong(song);
        return Result.success();
    }

    /**
     * 删除歌曲
     */
    @ApiOperation("删除歌曲")
    @DeleteMapping("/{id}")
    public Result<Void> deleteSong(@ApiParam("歌曲ID") @PathVariable Long id) {
        songService.deleteSong(id);
        return Result.success();
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
}
