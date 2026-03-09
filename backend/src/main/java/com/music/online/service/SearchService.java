package com.music.online.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.music.online.entity.Album;
import com.music.online.entity.Artist;
import com.music.online.entity.Playlist;
import com.music.online.entity.Song;
import com.music.online.mapper.AlbumMapper;
import com.music.online.mapper.ArtistMapper;
import com.music.online.mapper.PlaylistMapper;
import com.music.online.mapper.SongMapper;
import com.music.online.vo.PlaylistVO;
import com.music.online.vo.SearchResultVO;
import com.music.online.vo.SongVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 搜索服务
 *
 * @since 2026-03-09
 */
@Service
@RequiredArgsConstructor
public class SearchService {

    private final ArtistMapper artistMapper;
    private final AlbumMapper albumMapper;
    private final SongMapper songMapper;
    private final PlaylistMapper playlistMapper;
    private final SongService songService;
    private final PlaylistService playlistService;

    /**
     * 综合搜索
     */
    public SearchResultVO search(String keyword) {
        SearchResultVO result = new SearchResultVO();

        // 搜索歌手
        LambdaQueryWrapper<Artist> artistWrapper = new LambdaQueryWrapper<>();
        artistWrapper.and(w -> w.like(Artist::getName, keyword)
                .or().like(Artist::getNameEn, keyword));
        artistWrapper.eq(Artist::getStatus, 1);
        artistWrapper.last("LIMIT 10");
        result.setArtists(artistMapper.selectList(artistWrapper));

        // 搜索专辑
        LambdaQueryWrapper<Album> albumWrapper = new LambdaQueryWrapper<>();
        albumWrapper.like(Album::getName, keyword);
        albumWrapper.eq(Album::getStatus, 1);
        albumWrapper.last("LIMIT 10");
        List<Album> albums = albumMapper.selectList(albumWrapper);
        result.setAlbums(albums);

        // 搜索歌曲
        LambdaQueryWrapper<Song> songWrapper = new LambdaQueryWrapper<>();
        songWrapper.like(Song::getName, keyword);
        songWrapper.eq(Song::getStatus, 1);
        songWrapper.last("LIMIT 10");
        List<Song> songs = songMapper.selectList(songWrapper);
        List<SongVO> songVOs = songs.stream()
                .map(song -> songService.getSongById(song.getId()))
                .collect(Collectors.toList());
        result.setSongs(songVOs);

        // 搜索歌单
        LambdaQueryWrapper<Playlist> playlistWrapper = new LambdaQueryWrapper<>();
        playlistWrapper.and(w -> w.like(Playlist::getName, keyword)
                .or().like(Playlist::getDescription, keyword));
        playlistWrapper.eq(Playlist::getStatus, 1);
        playlistWrapper.last("LIMIT 10");
        List<Playlist> playlists = playlistMapper.selectList(playlistWrapper);
        List<PlaylistVO> playlistVOs = playlists.stream()
                .map(playlist -> playlistService.getPlaylistById(playlist.getId()))
                .collect(Collectors.toList());
        result.setPlaylists(playlistVOs);

        return result;
    }
}
