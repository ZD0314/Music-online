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
 * 歌手控制器
 *
 * @since 2026-03-09
 */
@Api(tags = "歌手管理接口")
@RestController
@RequestMapping("/admin/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    /**
     * 分页查询歌手列表
     */
    @ApiOperation("分页查询歌手列表")
    @GetMapping
    public Result<Page<Artist>> getArtistList(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer size,
            @ApiParam("歌手名称") @RequestParam(required = false) String name,
            @ApiParam("流派") @RequestParam(required = false) String genre,
            @ApiParam("语种") @RequestParam(required = false) String language
    ) {
        Page<Artist> artistPage = artistService.getArtistList(page, size, name, genre, language);
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

    /**
     * 创建歌手
     */
    @ApiOperation("创建歌手")
    @PostMapping
    public Result<Void> createArtist(@RequestBody Artist artist) {
        artistService.createArtist(artist);
        return Result.success();
    }

    /**
     * 更新歌手
     */
    @ApiOperation("更新歌手")
    @PutMapping("/{id}")
    public Result<Void> updateArtist(
            @ApiParam("歌手ID") @PathVariable Long id,
            @RequestBody Artist artist
    ) {
        artist.setId(id);
        artistService.updateArtist(artist);
        return Result.success();
    }

    /**
     * 删除歌手
     */
    @ApiOperation("删除歌手")
    @DeleteMapping("/{id}")
    public Result<Void> deleteArtist(@ApiParam("歌手ID") @PathVariable Long id) {
        artistService.deleteArtist(id);
        return Result.success();
    }
}
