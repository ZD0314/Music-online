package com.music.online.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.online.common.BusinessException;
import com.music.online.entity.Album;
import com.music.online.entity.Artist;
import com.music.online.mapper.AlbumMapper;
import com.music.online.mapper.ArtistMapper;
import com.music.online.vo.AlbumVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 专辑服务
 *
 * @since 2026-03-09
 */
@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumMapper albumMapper;
    private final ArtistMapper artistMapper;

    /**
     * 分页查询专辑列表
     */
    public Page<AlbumVO> getAlbumList(Integer page, Integer size, String name, Long artistId) {
        Page<Album> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Album> wrapper = new LambdaQueryWrapper<>();

        if (name != null && !name.isEmpty()) {
            wrapper.like(Album::getName, name);
        }
        if (artistId != null) {
            wrapper.eq(Album::getArtistId, artistId);
        }

        wrapper.eq(Album::getStatus, 1);
        wrapper.orderByDesc(Album::getCreateTime);

        Page<Album> albumPage = albumMapper.selectPage(pageParam, wrapper);

        // 转换为 VO
        Page<AlbumVO> voPage = new Page<>(albumPage.getCurrent(), albumPage.getSize(), albumPage.getTotal());
        voPage.setRecords(albumPage.getRecords().stream().map(this::convertToVO).toList());

        return voPage;
    }

    /**
     * 获取专辑详情
     */
    public AlbumVO getAlbumById(Long id) {
        Album album = albumMapper.selectById(id);
        if (album == null || album.getStatus() == 0) {
            throw new BusinessException(404, "专辑不存在");
        }
        return convertToVO(album);
    }

    /**
     * 创建专辑
     */
    public void createAlbum(Album album) {
        // 验证歌手是否存在
        Artist artist = artistMapper.selectById(album.getArtistId());
        if (artist == null) {
            throw new BusinessException(400, "歌手不存在");
        }

        album.setStatus(1);
        albumMapper.insert(album);
    }

    /**
     * 更新专辑
     */
    public void updateAlbum(Album album) {
        Album existAlbum = albumMapper.selectById(album.getId());
        if (existAlbum == null) {
            throw new BusinessException(404, "专辑不存在");
        }

        // 验证歌手是否存在
        if (album.getArtistId() != null) {
            Artist artist = artistMapper.selectById(album.getArtistId());
            if (artist == null) {
                throw new BusinessException(400, "歌手不存在");
            }
        }

        albumMapper.updateById(album);
    }

    /**
     * 删除专辑
     */
    public void deleteAlbum(Long id) {
        Album album = albumMapper.selectById(id);
        if (album == null) {
            throw new BusinessException(404, "专辑不存在");
        }
        albumMapper.deleteById(id);
    }

    /**
     * 转换为 VO
     */
    private AlbumVO convertToVO(Album album) {
        AlbumVO vo = new AlbumVO();
        BeanUtils.copyProperties(album, vo);

        // 查询歌手名称
        Artist artist = artistMapper.selectById(album.getArtistId());
        if (artist != null) {
            vo.setArtistName(artist.getName());
        }

        return vo;
    }
}
