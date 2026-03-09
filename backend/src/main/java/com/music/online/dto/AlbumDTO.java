package com.music.online.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 专辑创建/更新请求
 *
 * @since 2026-03-09
 */
@Data
@ApiModel("专辑创建/更新请求")
public class AlbumDTO {

    @ApiModelProperty(value = "专辑名", required = true)
    @NotBlank(message = "专辑名不能为空")
    private String name;

    @ApiModelProperty(value = "歌手ID", required = true)
    @NotNull(message = "歌手ID不能为空")
    private Long artistId;

    @ApiModelProperty(value = "封面URL")
    private String cover;

    @ApiModelProperty(value = "发行日期")
    private LocalDate releaseDate;

    @ApiModelProperty(value = "专辑描述")
    private String description;

    @ApiModelProperty(value = "状态: 1-正常 0-下架")
    private Integer status;
}
