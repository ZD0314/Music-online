package com.music.online.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 歌曲响应
 *
 * @since 2026-03-09
 */
@Data
@ApiModel("歌曲响应")
public class SongVO {

    @ApiModelProperty("歌曲ID")
    private Long id;

    @ApiModelProperty("歌曲名")
    private String name;

    @ApiModelProperty("歌手ID")
    private Long artistId;

    @ApiModelProperty("歌手名")
    private String artistName;

    @ApiModelProperty("专辑ID")
    private Long albumId;

    @ApiModelProperty("专辑名")
    private String albumName;

    @ApiModelProperty("封面图URL")
    private String cover;

    @ApiModelProperty("音频文件URL")
    private String audioUrl;

    @ApiModelProperty("歌词文件URL")
    private String lyricUrl;

    @ApiModelProperty("时长(秒)")
    private Integer duration;

    @ApiModelProperty("播放次数")
    private Integer playCount;

    @ApiModelProperty("状态: 1-正常 0-下架")
    private Integer status;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
