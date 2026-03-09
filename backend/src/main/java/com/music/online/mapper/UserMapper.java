package com.music.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.music.online.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper
 *
 * @author Claude
 * @since 2026-03-09
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
