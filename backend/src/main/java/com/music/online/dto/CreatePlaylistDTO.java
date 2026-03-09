package com.music.online.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 创建歌单DTO
 */
@Data
@ApiModel("创建歌单DTO")
public class CreatePlaylistDTO {

    @ApiModelProperty(value = "歌单名称", required = true)
    @NotBlank(message = "歌单名称不能为空")
    private String name;

    @ApiModelProperty("歌单封面")
    private String cover;

    @ApiModelProperty("歌单描述")
    private String description;

    @ApiModelProperty("歌单风格")
    private String style;
}
