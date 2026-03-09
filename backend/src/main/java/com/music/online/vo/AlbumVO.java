package com.music.online.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 专辑响应
 *
 * @since 2026-03-09
 */
@Data
@ApiModel("专辑响应")
public class AlbumVO {

    @ApiModelProperty("专辑ID")
    private Long id;

    @ApiModelProperty("专辑名")
    private String name;

    @ApiModelProperty("歌手ID")
    private Long artistId;

    @ApiModelProperty("歌手名")
    private String artistName;

    @ApiModelProperty("封面URL")
    private String cover;

    @ApiModelProperty("发行日期")
    private LocalDate releaseDate;

    @ApiModelProperty("专辑描述")
    private String description;

    @ApiModelProperty("状态: 1-正常 0-下架")
    private Integer status;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
