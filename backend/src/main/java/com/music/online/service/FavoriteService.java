package com.music.online.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.music.online.common.BusinessException;
import com.music.online.dto.FavoriteDTO;
import com.music.online.entity.*;
import com.music.online.mapper.*;
import com.music.online.vo.FavoriteVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 收藏服务
 *
 * @since 2026-03-09
 */
@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final SongMapper songMapper;
    private final AlbumMapper albumMapper;
    private final PlaylistMapper playlistMapper;
    private final ArtistMapper artistMapper;

    /**
     * 添加收藏
     */
    public void addFavorite(Long userId, FavoriteDTO dto) {
        // 检查是否已收藏
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId);
        wrapper.eq(Favorite::getType, dto.getType());
        wrapper.eq(Favorite::getTargetId, dto.getTargetId());
        if (favoriteMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(400, "已经收藏过了");
        }

        // 验证目标是否存在
        validateTarget(dto.getType(), dto.getTargetId());

        // 添加收藏
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setType(dto.getType());
        favorite.setTargetId(dto.getTargetId());
        favorite.setCreateTime(LocalDateTime.now());
        favoriteMapper.insert(favorite);
    }

    /**
     * 取消收藏
     */
    public void removeFavorite(Long userId, Long favoriteId) {
        Favorite favorite = favoriteMapper.selectById(favoriteId);
        if (favorite == null) {
            throw new BusinessException(404, "收藏不存在");
        }
        if (!favorite.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权操作");
        }
        favoriteMapper.deleteById(favoriteId);
    }

    /**
     * 获取收藏列表
     */
    public List<FavoriteVO> getFavoriteList(Long userId, Integer type) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId);
        if (type != null) {
            wrapper.eq(Favorite::getType, type);
        }
        wrapper.orderByDesc(Favorite::getCreateTime);

        List<Favorite> favorites = favoriteMapper.selectList(wrapper);
        List<FavoriteVO> result = new ArrayList<>();

        for (Favorite favorite : favorites) {
            FavoriteVO vo = convertToVO(favorite);
            if (vo != null) {
                result.add(vo);
            }
        }

        return result;
    }

    /**
     * 检查是否已收藏
     */
    public boolean isFavorited(Long userId, Integer type, Long targetId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId);
        wrapper.eq(Favorite::getType, type);
        wrapper.eq(Favorite::getTargetId, targetId);
        return favoriteMapper.selectCount(wrapper) > 0;
    }

    /**
     * 验证目标是否存在
     */
    private void validateTarget(Integer type, Long targetId) {
        switch (type) {
            case 1: // 歌曲
                Song song = songMapper.selectById(targetId);
                if (song == null || song.getStatus() == 0) {
                    throw new BusinessException(404, "歌曲不存在");
                }
                break;
            case 2: // 专辑
                Album album = albumMapper.selectById(targetId);
                if (album == null || album.getStatus() == 0) {
                    throw new BusinessException(404, "专辑不存在");
                }
                break;
            case 3: // 歌单
                Playlist playlist = playlistMapper.selectById(targetId);
                if (playlist == null || playlist.getStatus() == 0) {
                    throw new BusinessException(404, "歌单不存在");
                }
                break;
            default:
                throw new BusinessException(400, "收藏类型错误");
        }
    }

    /**
     * 转换为VO
     */
    private FavoriteVO convertToVO(Favorite favorite) {
        FavoriteVO vo = new FavoriteVO();
        vo.setId(favorite.getId());
        vo.setUserId(favorite.getUserId());
        vo.setType(favorite.getType());
        vo.setTargetId(favorite.getTargetId());
        vo.setCreateTime(favorite.getCreateTime());

        switch (favorite.getType()) {
            case 1: // 歌曲
                Song song = songMapper.selectById(favorite.getTargetId());
                if (song != null && song.getStatus() == 1) {
                    vo.setTargetName(song.getName());
                    vo.setTargetCover(song.getCover());
                    Artist artist = artistMapper.selectById(song.getArtistId());
                    if (artist != null) {
                        vo.setArtistName(artist.getName());
                    }
                } else {
                    return null;
                }
                break;
            case 2: // 专辑
                Album album = albumMapper.selectById(favorite.getTargetId());
                if (album != null && album.getStatus() == 1) {
                    vo.setTargetName(album.getName());
                    vo.setTargetCover(album.getCover());
                    Artist artist = artistMapper.selectById(album.getArtistId());
                    if (artist != null) {
                        vo.setArtistName(artist.getName());
                    }
                } else {
                    return null;
                }
                break;
            case 3: // 歌单
                Playlist playlist = playlistMapper.selectById(favorite.getTargetId());
                if (playlist != null && playlist.getStatus() == 1) {
                    vo.setTargetName(playlist.getName());
                    vo.setTargetCover(playlist.getCover());
                } else {
                    return null;
                }
                break;
        }

        return vo;
    }
}
