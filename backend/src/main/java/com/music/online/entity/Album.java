package com.music.online.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 专辑实体
 *
 * @since 2026-03-09
 */
@Data
@TableName("album")
public class Album implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 专辑ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 专辑名
     */
    private String name;

    /**
     * 歌手ID
     */
    private Long artistId;

    /**
     * 封面图URL
     */
    private String cover;

    /**
     * 发行日期
     */
    private LocalDate releaseDate;

    /**
     * 专辑简介
     */
    private String description;

    /**
     * 状态: 1-正常 0-下架
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
