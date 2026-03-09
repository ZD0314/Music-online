package com.music.online.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 歌单实体
 *
 * @since 2026-03-09
 */
@Data
@TableName("playlist")
public class Playlist implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 歌单ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 歌单名称
     */
    private String name;

    /**
     * 歌单封面
     */
    private String cover;

    /**
     * 歌单描述
     */
    private String description;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 创建者类型: 1-管理员 2-用户
     */
    private Integer creatorType;

    /**
     * 标签(逗号分隔)
     */
    private String tags;

    /**
     * 播放次数
     */
    private Long playCount;

    /**
     * 收藏次数
     */
    private Long collectCount;

    /**
     * 状态: 1-公开 0-私密
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
