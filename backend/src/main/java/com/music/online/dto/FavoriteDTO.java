package com.music.online.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 添加收藏请求
 *
 * @since 2026-03-09
 */
@Data
@ApiModel("添加收藏请求")
public class FavoriteDTO {

    @ApiModelProperty(value = "收藏类型: 1-歌曲 2-专辑 3-歌单", required = true)
    @NotNull(message = "收藏类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "目标ID", required = true)
    @NotNull(message = "目标ID不能为空")
    private Long targetId;
}
