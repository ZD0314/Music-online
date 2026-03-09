package com.music.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.music.online.entity.Album;
import org.apache.ibatis.annotations.Mapper;

/**
 * 专辑 Mapper
 *
 * @since 2026-03-09
 */
@Mapper
public interface AlbumMapper extends BaseMapper<Album> {
}
