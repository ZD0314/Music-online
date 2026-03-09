package com.music.online.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.online.common.Result;
import com.music.online.dto.AlbumDTO;
import com.music.online.entity.Album;
import com.music.online.service.AlbumService;
import com.music.online.vo.AlbumVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 专辑控制器
 *
 * @since 2026-03-09
 */
@Api(tags = "专辑管理接口")
@RestController
@RequestMapping("/admin/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    /**
     * 分页查询专辑列表
     */
    @ApiOperation("分页查询专辑列表")
    @GetMapping
    public Result<Page<AlbumVO>> getAlbumList(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer size,
            @ApiParam("专辑名称") @RequestParam(required = false) String name,
            @ApiParam("歌手ID") @RequestParam(required = false) Long artistId
    ) {
        Page<AlbumVO> albumPage = albumService.getAlbumList(page, size, name, artistId);
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

    /**
     * 创建专辑
     */
    @ApiOperation("创建专辑")
    @PostMapping
    public Result<Void> createAlbum(@Valid @RequestBody AlbumDTO albumDTO) {
        Album album = new Album();
        BeanUtils.copyProperties(albumDTO, album);
        albumService.createAlbum(album);
        return Result.success();
    }

    /**
     * 更新专辑
     */
    @ApiOperation("更新专辑")
    @PutMapping("/{id}")
    public Result<Void> updateAlbum(
            @ApiParam("专辑ID") @PathVariable Long id,
            @Valid @RequestBody AlbumDTO albumDTO
    ) {
        Album album = new Album();
        BeanUtils.copyProperties(albumDTO, album);
        album.setId(id);
        albumService.updateAlbum(album);
        return Result.success();
    }

    /**
     * 删除专辑
     */
    @ApiOperation("删除专辑")
    @DeleteMapping("/{id}")
    public Result<Void> deleteAlbum(@ApiParam("专辑ID") @PathVariable Long id) {
        albumService.deleteAlbum(id);
        return Result.success();
    }
}
