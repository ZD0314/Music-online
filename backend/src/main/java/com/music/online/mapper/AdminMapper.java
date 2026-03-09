package com.music.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.music.online.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员 Mapper
 *
 * @author Claude
 * @since 2026-03-09
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
}
