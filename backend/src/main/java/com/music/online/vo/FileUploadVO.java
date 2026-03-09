package com.music.online.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件上传响应
 *
 * @since 2026-03-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("文件上传响应")
public class FileUploadVO {

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件URL")
    private String fileUrl;

    @ApiModelProperty("文件大小(字节)")
    private Long fileSize;
}
