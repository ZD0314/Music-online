package com.music.online.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.online.common.Result;
import com.music.online.entity.Artist;
import com.music.online.service.ArtistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 歌手控制器 - 用户端
 *
 * @since 2026-03-09
 */
@Api(tags = "歌手接口 - 用户端")
@RestController
@RequestMapping("/api/artists")
@RequiredArgsConstructor
public class UserArtistController {

    private final ArtistService artistService;

    /**
     * 获取歌手列表
     */
    @ApiOperation("获取歌手列表")
    @GetMapping
    public Result<Page<Artist>> getArtists(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer size,
            @ApiParam("搜索关键词") @RequestParam(required = false) String keyword,
            @ApiParam("首字母") @RequestParam(required = false) String initial,
            @ApiParam("语种") @RequestParam(required = false) String language,
            @ApiParam("流派") @RequestParam(required = false) String genre
    ) {
        Page<Artist> artistPage = artistService.getArtistList(page, size, keyword, genre, language);
        return Result.success(artistPage);
    }

    /**
     * 获取歌手详情
     */
    @ApiOperation("获取歌手详情")
    @GetMapping("/{id}")
    public Result<Artist> getArtistById(@ApiParam("歌手ID") @PathVariable Long id) {
        Artist artist = artistService.getArtistById(id);
        return Result.success(artist);
    }
}
