package com.music.online.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 歌曲创建/更新请求
 *
 * @since 2026-03-09
 */
@Data
@ApiModel("歌曲创建/更新请求")
public class SongDTO {

    @ApiModelProperty(value = "歌曲名", required = true)
    @NotBlank(message = "歌曲名不能为空")
    private String name;

    @ApiModelProperty(value = "歌手ID", required = true)
    @NotNull(message = "歌手ID不能为空")
    private Long artistId;

    @ApiModelProperty(value = "专辑ID")
    private Long albumId;

    @ApiModelProperty(value = "封面图URL")
    private String cover;

    @ApiModelProperty(value = "音频文件URL")
    private String audioUrl;

    @ApiModelProperty(value = "歌词文件URL")
    private String lyricUrl;

    @ApiModelProperty(value = "时长(秒)")
    private Integer duration;

    @ApiModelProperty(value = "状态: 1-正常 0-下架")
    private Integer status;
}
