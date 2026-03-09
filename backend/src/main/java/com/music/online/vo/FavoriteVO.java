package com.music.online.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 收藏VO
 *
 * @since 2026-03-09
 */
@Data
public class FavoriteVO {

    /**
     * 收藏ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 收藏类型: 1-歌曲 2-专辑 3-歌单
     */
    private Integer type;

    /**
     * 目标ID
     */
    private Long targetId;

    /**
     * 目标名称
     */
    private String targetName;

    /**
     * 目标封面
     */
    private String targetCover;

    /**
     * 歌手名称（歌曲/专辑）
     */
    private String artistName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
