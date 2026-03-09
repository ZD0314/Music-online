package com.music.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.music.online.entity.Playlist;
import org.apache.ibatis.annotations.Mapper;

/**
 * 歌单Mapper
 *
 * @since 2026-03-09
 */
@Mapper
public interface PlaylistMapper extends BaseMapper<Playlist> {
}
