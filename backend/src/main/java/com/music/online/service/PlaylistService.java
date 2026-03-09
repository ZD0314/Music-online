package com.music.online.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.online.common.BusinessException;
import com.music.online.dto.CreatePlaylistDTO;
import com.music.online.dto.PlaylistDTO;
import com.music.online.entity.Playlist;
import com.music.online.entity.PlaylistSong;
import com.music.online.entity.Song;
import com.music.online.entity.User;
import com.music.online.mapper.PlaylistMapper;
import com.music.online.mapper.PlaylistSongMapper;
import com.music.online.mapper.SongMapper;
import com.music.online.mapper.UserMapper;
import com.music.online.vo.PlaylistVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 歌单服务
 *
 * @since 2026-03-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistMapper playlistMapper;
    private final PlaylistSongMapper playlistSongMapper;
    private final SongMapper songMapper;
    private final UserMapper userMapper;

    /**
     * 分页查询歌单列表
     */
    public Page<PlaylistVO> getPlaylistList(Integer page, Integer size, String name) {
        Page<Playlist> playlistPage = new Page<>(page, size);
        LambdaQueryWrapper<Playlist> wrapper = new LambdaQueryWrapper<>();

        if (name != null && !name.isEmpty()) {
            wrapper.like(Playlist::getName, name);
        }

        wrapper.orderByDesc(Playlist::getCreateTime);
        playlistMapper.selectPage(playlistPage, wrapper);

        // 转换为VO
        Page<PlaylistVO> voPage = new Page<>(page, size, playlistPage.getTotal());
        List<PlaylistVO> voList = playlistPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    /**
     * 获取歌单详情
     */
    public PlaylistVO getPlaylistById(Long id) {
        Playlist playlist = playlistMapper.selectById(id);
        if (playlist == null) {
            throw new BusinessException(404, "歌单不存在");
        }
        return convertToVO(playlist);
    }

    /**
     * 创建歌单
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createPlaylist(PlaylistDTO dto) {
        // 验证创建者类型
        if (dto.getCreatorType() == 2) {
            // 如果是用户创建,验证用户是否存在
            User user = userMapper.selectById(dto.getCreatorId());
            if (user == null) {
                throw new BusinessException(400, "用户不存在");
            }
        }

        Playlist playlist = new Playlist();
        BeanUtils.copyProperties(dto, playlist);
        playlist.setPlayCount(0L);
        playlist.setCollectCount(0L);
        playlist.setCreateTime(LocalDateTime.now());
        playlist.setUpdateTime(LocalDateTime.now());

        playlistMapper.insert(playlist);
        log.info("创建歌单成功: {}", playlist.getId());
        return playlist.getId();
    }

    /**
     * 更新歌单
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePlaylist(Long id, PlaylistDTO dto) {
        Playlist playlist = playlistMapper.selectById(id);
        if (playlist == null) {
            throw new BusinessException(404, "歌单不存在");
        }

        BeanUtils.copyProperties(dto, playlist);
        playlist.setUpdateTime(LocalDateTime.now());

        playlistMapper.updateById(playlist);
        log.info("更新歌单成功: {}", id);
    }

    /**
     * 删除歌单
     */
    @Transactional(rollbackFor = Exception.class)
    public void deletePlaylist(Long id) {
        Playlist playlist = playlistMapper.selectById(id);
        if (playlist == null) {
            throw new BusinessException(404, "歌单不存在");
        }

        // 删除歌单中的所有歌曲关联
        LambdaQueryWrapper<PlaylistSong> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PlaylistSong::getPlaylistId, id);
        playlistSongMapper.delete(wrapper);

        // 删除歌单
        playlistMapper.deleteById(id);
        log.info("删除歌单成功: {}", id);
    }

    /**
     * 添加歌曲到歌单
     */
    @Transactional(rollbackFor = Exception.class)
    public void addSongToPlaylist(Long playlistId, Long songId) {
        // 验证歌单是否存在
        Playlist playlist = playlistMapper.selectById(playlistId);
        if (playlist == null) {
            throw new BusinessException(404, "歌单不存在");
        }

        // 验证歌曲是否存在
        Song song = songMapper.selectById(songId);
        if (song == null) {
            throw new BusinessException(404, "歌曲不存在");
        }

        // 检查歌曲是否已在歌单中
        LambdaQueryWrapper<PlaylistSong> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PlaylistSong::getPlaylistId, playlistId)
                .eq(PlaylistSong::getSongId, songId);
        Long count = playlistSongMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException(400, "歌曲已在歌单中");
        }

        // 获取当前最大排序号
        LambdaQueryWrapper<PlaylistSong> sortWrapper = new LambdaQueryWrapper<>();
        sortWrapper.eq(PlaylistSong::getPlaylistId, playlistId)
                .orderByDesc(PlaylistSong::getSortOrder)
                .last("LIMIT 1");
        PlaylistSong lastSong = playlistSongMapper.selectOne(sortWrapper);
        int nextSortOrder = (lastSong != null) ? lastSong.getSortOrder() + 1 : 1;

        // 添加歌曲到歌单
        PlaylistSong playlistSong = new PlaylistSong();
        playlistSong.setPlaylistId(playlistId);
        playlistSong.setSongId(songId);
        playlistSong.setSortOrder(nextSortOrder);
        playlistSong.setCreateTime(LocalDateTime.now());

        playlistSongMapper.insert(playlistSong);
        log.info("添加歌曲到歌单成功: playlistId={}, songId={}", playlistId, songId);
    }

    /**
     * 从歌单移除歌曲
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeSongFromPlaylist(Long playlistId, Long songId) {
        LambdaQueryWrapper<PlaylistSong> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PlaylistSong::getPlaylistId, playlistId)
                .eq(PlaylistSong::getSongId, songId);

        int deleted = playlistSongMapper.delete(wrapper);
        if (deleted == 0) {
            throw new BusinessException(404, "歌曲不在歌单中");
        }

        log.info("从歌单移除歌曲成功: playlistId={}, songId={}", playlistId, songId);
    }

    /**
     * 转换为VO
     */
    private PlaylistVO convertToVO(Playlist playlist) {
        PlaylistVO vo = new PlaylistVO();
        BeanUtils.copyProperties(playlist, vo);

        // 获取创建者名称
        if (playlist.getCreatorType() == 1) {
            vo.setCreatorName("管理员");
        } else if (playlist.getCreatorType() == 2) {
            User user = userMapper.selectById(playlist.getCreatorId());
            if (user != null) {
                vo.setCreatorName(user.getUsername());
            }
        }

        // 获取歌曲数量
        LambdaQueryWrapper<PlaylistSong> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PlaylistSong::getPlaylistId, playlist.getId());
        Long songCount = playlistSongMapper.selectCount(wrapper);
        vo.setSongCount(songCount.intValue());

        return vo;
    }

    /**
     * 创建用户歌单
     */
    @Transactional
    public void createUserPlaylist(Long userId, CreatePlaylistDTO dto) {
        Playlist playlist = new Playlist();
        playlist.setName(dto.getName());
        playlist.setCover(dto.getCover());
        playlist.setDescription(dto.getDescription());
        playlist.setCreatorId(userId);
        playlist.setCreatorType(2); // 2-用户创建
        playlist.setTags(dto.getStyle());
        playlist.setPlayCount(0L);
        playlist.setCollectCount(0L);
        playlist.setStatus(1);

        playlistMapper.insert(playlist);
        log.info("创建用户歌单成功: userId={}, playlistId={}", userId, playlist.getId());
    }

    /**
     * 获取用户歌单列表
     */
    public List<PlaylistVO> getUserPlaylists(Long userId) {
        LambdaQueryWrapper<Playlist> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Playlist::getCreatorId, userId)
                .eq(Playlist::getCreatorType, 2)
                .eq(Playlist::getStatus, 1)
                .orderByDesc(Playlist::getCreateTime);

        List<Playlist> playlists = playlistMapper.selectList(wrapper);
        return playlists.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 删除用户歌单
     */
    @Transactional
    public void deleteUserPlaylist(Long userId, Long playlistId) {
        Playlist playlist = playlistMapper.selectById(playlistId);
        if (playlist == null) {
            throw new BusinessException(404, "歌单不存在");
        }

        // 验证是否是用户自己的歌单
        if (!playlist.getCreatorId().equals(userId) || playlist.getCreatorType() != 2) {
            throw new BusinessException(403, "无权删除此歌单");
        }

        // 删除歌单
        playlistMapper.deleteById(playlistId);

        // 删除歌单中的所有歌曲关联
        LambdaQueryWrapper<PlaylistSong> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PlaylistSong::getPlaylistId, playlistId);
        playlistSongMapper.delete(wrapper);

        log.info("删除用户歌单成功: userId={}, playlistId={}", userId, playlistId);
    }

    /**
     * 更新用户歌单
     */
    @Transactional
    public void updateUserPlaylist(Long userId, Long playlistId, CreatePlaylistDTO dto) {
        Playlist playlist = playlistMapper.selectById(playlistId);
        if (playlist == null) {
            throw new BusinessException(404, "歌单不存在");
        }

        // 验证是否是用户自己的歌单
        if (!playlist.getCreatorId().equals(userId) || playlist.getCreatorType() != 2) {
            throw new BusinessException(403, "无权修改此歌单");
        }

        // 更新歌单信息
        playlist.setName(dto.getName());
        playlist.setCover(dto.getCover());
        playlist.setDescription(dto.getDescription());
        playlist.setTags(dto.getStyle());

        playlistMapper.updateById(playlist);
        log.info("更新用户歌单成功: userId={}, playlistId={}", userId, playlistId);
    }
}
