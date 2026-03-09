package com.music.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.music.online.entity.Artist;
import org.apache.ibatis.annotations.Mapper;

/**
 * 歌手 Mapper
 *
 * @author Claude
 * @since 2026-03-09
 */
@Mapper
public interface ArtistMapper extends BaseMapper<Artist> {
}
