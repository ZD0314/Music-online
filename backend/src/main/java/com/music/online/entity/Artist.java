package com.music.online.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 歌手实体
 *
 * @since 2026-03-09
 */
@Data
@TableName("artist")
public class Artist implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 歌手ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 歌手名
     */
    private String name;

    /**
     * 外文名
     */
    private String nameEn;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 流派: 流行/摇滚/民谣/说唱/电子/古典
     */
    private String genre;

    /**
     * 语种: 华语/欧美/日韩/其他
     */
    private String language;

    /**
     * 首字母(A-Z)
     */
    private String initial;

    /**
     * 个人简介
     */
    private String introduction;

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
