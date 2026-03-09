package com.music.online.controller;

import com.music.online.common.Result;
import com.music.online.service.FileUploadService;
import com.music.online.vo.FileUploadVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传控制器
 *
 * @since 2026-03-09
 */
@Api(tags = "文件上传接口")
@RestController
@RequestMapping("/admin/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    /**
     * 上传图片
     */
    @ApiOperation("上传图片")
    @PostMapping("/image")
    public Result<FileUploadVO> uploadImage(
            @ApiParam("图片文件") @RequestParam("file") MultipartFile file
    ) {
        FileUploadVO result = fileUploadService.uploadImage(file);
        return Result.success(result);
    }

    /**
     * 上传音频
     */
    @ApiOperation("上传音频")
    @PostMapping("/audio")
    public Result<FileUploadVO> uploadAudio(
            @ApiParam("音频文件") @RequestParam("file") MultipartFile file
    ) {
        FileUploadVO result = fileUploadService.uploadAudio(file);
        return Result.success(result);
    }

    /**
     * 上传歌词
     */
    @ApiOperation("上传歌词")
    @PostMapping("/lyric")
    public Result<FileUploadVO> uploadLyric(
            @ApiParam("歌词文件") @RequestParam("file") MultipartFile file
    ) {
        FileUploadVO result = fileUploadService.uploadLyric(file);
        return Result.success(result);
    }
}
