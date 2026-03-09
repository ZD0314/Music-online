package com.music.online.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.online.common.BusinessException;
import com.music.online.entity.Artist;
import com.music.online.mapper.ArtistMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 歌手服务
 *
 * @author Claude
 * @since 2026-03-09
 */
@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistMapper artistMapper;

    /**
     * 分页查询歌手列表
     */
    public Page<Artist> getArtistList(Integer page, Integer size, String name, String genre, String language) {
        Page<Artist> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Artist> wrapper = new LambdaQueryWrapper<>();

        if (name != null && !name.isEmpty()) {
            wrapper.like(Artist::getName, name);
        }
        if (genre != null && !genre.isEmpty()) {
            wrapper.eq(Artist::getGenre, genre);
        }
        if (language != null && !language.isEmpty()) {
            wrapper.eq(Artist::getLanguage, language);
        }

        wrapper.eq(Artist::getStatus, 1);
        wrapper.orderByDesc(Artist::getCreateTime);

        return artistMapper.selectPage(pageParam, wrapper);
    }

    /**
     * 获取歌手详情
     */
    public Artist getArtistById(Long id) {
        Artist artist = artistMapper.selectById(id);
        if (artist == null || artist.getStatus() == 0) {
            throw new BusinessException(404, "歌手不存在");
        }
        return artist;
    }

    /**
     * 创建歌手
     */
    public void createArtist(Artist artist) {
        artist.setStatus(1);
        artistMapper.insert(artist);
    }

    /**
     * 更新歌手
     */
    public void updateArtist(Artist artist) {
        Artist existArtist = artistMapper.selectById(artist.getId());
        if (existArtist == null) {
            throw new BusinessException(404, "歌手不存在");
        }
        artistMapper.updateById(artist);
    }

    /**
     * 删除歌手
     */
    public void deleteArtist(Long id) {
        Artist artist = artistMapper.selectById(id);
        if (artist == null) {
            throw new BusinessException(404, "歌手不存在");
        }
        artistMapper.deleteById(id);
    }
}
