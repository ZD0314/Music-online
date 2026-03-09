package com.music.online.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.music.online.common.BusinessException;
import com.music.online.entity.Album;
import com.music.online.entity.Artist;
import com.music.online.entity.PlaylistSong;
import com.music.online.entity.Song;
import com.music.online.mapper.AlbumMapper;
import com.music.online.mapper.ArtistMapper;
import com.music.online.mapper.PlaylistSongMapper;
import com.music.online.mapper.SongMapper;
import com.music.online.vo.SongVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 歌曲服务
 *
 * @since 2026-03-09
 */
@Service
@RequiredArgsConstructor
public class SongService {

    private final SongMapper songMapper;
    private final ArtistMapper artistMapper;
    private final AlbumMapper albumMapper;
    private final PlaylistSongMapper playlistSongMapper;

    /**
     * 分页查询歌曲列表
     */
    public Page<SongVO> getSongList(Integer page, Integer size, String name, Long artistId, Long albumId) {
        Page<Song> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Song> wrapper = new LambdaQueryWrapper<>();

        if (name != null && !name.isEmpty()) {
            wrapper.like(Song::getName, name);
        }
        if (artistId != null) {
            wrapper.eq(Song::getArtistId, artistId);
        }
        if (albumId != null) {
            wrapper.eq(Song::getAlbumId, albumId);
        }

        wrapper.eq(Song::getStatus, 1);
        wrapper.orderByDesc(Song::getCreateTime);

        Page<Song> songPage = songMapper.selectPage(pageParam, wrapper);

        // 转换为 VO
        Page<SongVO> voPage = new Page<>(songPage.getCurrent(), songPage.getSize(), songPage.getTotal());
        voPage.setRecords(songPage.getRecords().stream().map(this::convertToVO).toList());

        return voPage;
    }

    /**
     * 获取歌曲详情
     */
    public SongVO getSongById(Long id) {
        Song song = songMapper.selectById(id);
        if (song == null || song.getStatus() == 0) {
            throw new BusinessException(404, "歌曲不存在");
        }
        return convertToVO(song);
    }

    /**
     * 创建歌曲
     */
    public void createSong(Song song) {
        // 验证歌手是否存在
        Artist artist = artistMapper.selectById(song.getArtistId());
        if (artist == null) {
            throw new BusinessException(400, "歌手不存在");
        }

        // 验证专辑是否存在（如果提供了专辑ID）
        if (song.getAlbumId() != null) {
            Album album = albumMapper.selectById(song.getAlbumId());
            if (album == null) {
                throw new BusinessException(400, "专辑不存在");
            }
        }

        song.setStatus(1);
        song.setPlayCount(0);
        songMapper.insert(song);
    }

    /**
     * 更新歌曲
     */
    public void updateSong(Song song) {
        Song existSong = songMapper.selectById(song.getId());
        if (existSong == null) {
            throw new BusinessException(404, "歌曲不存在");
        }

        // 验证歌手是否存在
        if (song.getArtistId() != null) {
            Artist artist = artistMapper.selectById(song.getArtistId());
            if (artist == null) {
                throw new BusinessException(400, "歌手不存在");
            }
        }

        // 验证专辑是否存在（如果提供了专辑ID）
        if (song.getAlbumId() != null) {
            Album album = albumMapper.selectById(song.getAlbumId());
            if (album == null) {
                throw new BusinessException(400, "专辑不存在");
            }
        }

        songMapper.updateById(song);
    }

    /**
     * 删除歌曲
     */
    public void deleteSong(Long id) {
        Song song = songMapper.selectById(id);
        if (song == null) {
            throw new BusinessException(404, "歌曲不存在");
        }
        songMapper.deleteById(id);
    }

    /**
     * 增加播放次数
     */
    public void incrementPlayCount(Long id) {
        Song song = songMapper.selectById(id);
        if (song == null || song.getStatus() == 0) {
            throw new BusinessException(404, "歌曲不存在");
        }
        song.setPlayCount(song.getPlayCount() + 1);
        songMapper.updateById(song);
    }

    /**
     * 根据歌单ID获取歌曲列表
     */
    public List<SongVO> getSongsByPlaylistId(Long playlistId) {
        // 查询歌单中的歌曲ID列表
        LambdaQueryWrapper<PlaylistSong> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PlaylistSong::getPlaylistId, playlistId);
        wrapper.orderByAsc(PlaylistSong::getSortOrder);
        List<PlaylistSong> playlistSongs = playlistSongMapper.selectList(wrapper);

        // 根据歌曲ID列表查询歌曲详情
        return playlistSongs.stream()
                .map(ps -> {
                    Song song = songMapper.selectById(ps.getSongId());
                    return song != null && song.getStatus() == 1 ? convertToVO(song) : null;
                })
                .filter(vo -> vo != null)
                .collect(Collectors.toList());
    }

    /**
     * 根据歌手ID获取热门歌曲
     */
    public List<SongVO> getHotSongsByArtistId(Long artistId, Integer limit) {
        LambdaQueryWrapper<Song> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Song::getArtistId, artistId);
        wrapper.eq(Song::getStatus, 1);
        wrapper.orderByDesc(Song::getPlayCount);
        wrapper.last("LIMIT " + limit);

        List<Song> songs = songMapper.selectList(wrapper);
        return songs.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 根据专辑ID获取歌曲列表
     */
    public List<SongVO> getSongsByAlbumId(Long albumId) {
        LambdaQueryWrapper<Song> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Song::getAlbumId, albumId);
        wrapper.eq(Song::getStatus, 1);
        wrapper.orderByDesc(Song::getCreateTime);

        List<Song> songs = songMapper.selectList(wrapper);
        return songs.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 转换为 VO
     */
    private SongVO convertToVO(Song song) {
        SongVO vo = new SongVO();
        BeanUtils.copyProperties(song, vo);

        // 查询歌手名称
        Artist artist = artistMapper.selectById(song.getArtistId());
        if (artist != null) {
            vo.setArtistName(artist.getName());
        }

        // 查询专辑名称
        if (song.getAlbumId() != null) {
            Album album = albumMapper.selectById(song.getAlbumId());
            if (album != null) {
                vo.setAlbumName(album.getName());
            }
        }

        return vo;
    }
}
