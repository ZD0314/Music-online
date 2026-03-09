package com.music.online.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 歌单DTO
 *
 * @since 2026-03-09
 */
@Data
public class PlaylistDTO {

    /**
     * 歌单名称
     */
    @NotBlank(message = "歌单名称不能为空")
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
    @NotNull(message = "创建者ID不能为空")
    private Long creatorId;

    /**
     * 创建者类型: 1-管理员 2-用户
     */
    @NotNull(message = "创建者类型不能为空")
    private Integer creatorType;

    /**
     * 标签(逗号分隔)
     */
    private String tags;

    /**
     * 状态: 1-公开 0-私密
     */
    private Integer status;
}
