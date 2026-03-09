package com.music.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.music.online.entity.PlaylistSong;
import org.apache.ibatis.annotations.Mapper;

/**
 * 歌单歌曲关联Mapper
 *
 * @since 2026-03-09
 */
@Mapper
public interface PlaylistSongMapper extends BaseMapper<PlaylistSong> {
}
