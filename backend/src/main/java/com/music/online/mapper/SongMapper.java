package com.music.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.music.online.entity.Song;
import org.apache.ibatis.annotations.Mapper;

/**
 * 歌曲 Mapper
 *
 * @since 2026-03-09
 */
@Mapper
public interface SongMapper extends BaseMapper<Song> {
}
