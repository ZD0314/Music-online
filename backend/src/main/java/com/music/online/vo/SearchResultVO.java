package com.music.online.vo;

import com.music.online.entity.Album;
import com.music.online.entity.Artist;
import lombok.Data;

import java.util.List;

/**
 * 搜索结果VO
 *
 * @since 2026-03-09
 */
@Data
public class SearchResultVO {

    /**
     * 歌手列表
     */
    private List<Artist> artists;

    /**
     * 专辑列表
     */
    private List<Album> albums;

    /**
     * 歌曲列表
     */
    private List<SongVO> songs;

    /**
     * 歌单列表
     */
    private List<PlaylistVO> playlists;
}
