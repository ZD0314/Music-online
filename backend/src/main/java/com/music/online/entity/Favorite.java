package com.music.online.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 收藏实体
 *
 * @since 2026-03-09
 */
@Data
@TableName("favorite")
public class Favorite implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 收藏ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 收藏类型: 1-歌曲 2-专辑 3-歌单
     */
    @TableField("target_type")
    private Integer type;

    /**
     * 目标ID（歌曲ID/专辑ID/歌单ID）
     */
    private Long targetId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
