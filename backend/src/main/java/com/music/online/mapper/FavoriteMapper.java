package com.music.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.music.online.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收藏Mapper
 *
 * @since 2026-03-09
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {
}
