package com.music.online.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 歌单VO
 *
 * @since 2026-03-09
 */
@Data
public class PlaylistVO {

    /**
     * 歌单ID
     */
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
     * 创建者名称
     */
    private String creatorName;

    /**
     * 标签
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
     * 歌曲数量
     */
    private Integer songCount;

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
