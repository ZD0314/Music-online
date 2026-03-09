package com.music.online.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 歌曲实体
 *
 * @since 2026-03-09
 */
@Data
@TableName("song")
public class Song implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 歌曲ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 歌曲名
     */
    private String name;

    /**
     * 歌手ID
     */
    private Long artistId;

    /**
     * 专辑ID
     */
    private Long albumId;

    /**
     * 音频文件URL
     */
    private String audioUrl;

    /**
     * 歌词文件URL
     */
    private String lyricUrl;

    /**
     * 封面图URL
     */
    private String cover;

    /**
     * 时长(秒)
     */
    private Integer duration;

    /**
     * 播放次数
     */
    private Integer playCount;

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
