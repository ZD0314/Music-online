package com.music.online.service;

import com.music.online.common.BusinessException;
import com.music.online.config.FileUploadConfig;
import com.music.online.vo.FileUploadVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传服务
 *
 * @since 2026-03-09
 */
@Slf4j
@Service
public class FileUploadService {

    /**
     * 允许的图片格式
     */
    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");

    /**
     * 允许的音频格式
     */
    private static final List<String> AUDIO_EXTENSIONS = Arrays.asList("mp3", "flac", "wav", "aac", "ogg", "m4a");

    /**
     * 允许的歌词格式
     */
    private static final List<String> LYRIC_EXTENSIONS = Arrays.asList("lrc", "txt");

    /**
     * 图片最大大小: 5MB
     */
    private static final long IMAGE_MAX_SIZE = 5 * 1024 * 1024;

    /**
     * 音频最大大小: 50MB
     */
    private static final long AUDIO_MAX_SIZE = 50 * 1024 * 1024;

    /**
     * 歌词最大大小: 1MB
     */
    private static final long LYRIC_MAX_SIZE = 1024 * 1024;

    /**
     * 上传图片
     */
    public FileUploadVO uploadImage(MultipartFile file) {
        validateFile(file, IMAGE_EXTENSIONS, IMAGE_MAX_SIZE, "图片");
        return saveFile(file, FileUploadConfig.IMAGE_DIR);
    }

    /**
     * 上传音频
     */
    public FileUploadVO uploadAudio(MultipartFile file) {
        validateFile(file, AUDIO_EXTENSIONS, AUDIO_MAX_SIZE, "音频");
        return saveFile(file, FileUploadConfig.AUDIO_DIR);
    }

    /**
     * 上传歌词
     */
    public FileUploadVO uploadLyric(MultipartFile file) {
        validateFile(file, LYRIC_EXTENSIONS, LYRIC_MAX_SIZE, "歌词");
        return saveFile(file, FileUploadConfig.LYRIC_DIR);
    }

    /**
     * 验证文件
     */
    private void validateFile(MultipartFile file, List<String> allowedExtensions, long maxSize, String fileType) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(400, "文件不能为空");
        }

        // 验证文件大小
        if (file.getSize() > maxSize) {
            throw new BusinessException(400, fileType + "文件大小不能超过 " + (maxSize / 1024 / 1024) + "MB");
        }

        // 验证文件扩展名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new BusinessException(400, "文件名无效");
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        if (!allowedExtensions.contains(extension)) {
            throw new BusinessException(400, "不支持的" + fileType + "格式，仅支持: " + String.join(", ", allowedExtensions));
        }
    }

    /**
     * 保存文件
     */
    private FileUploadVO saveFile(MultipartFile file, String directory) {
        try {
            // 创建目录
            File dir = new File(directory);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + extension;

            // 保存文件
            File destFile = new File(dir, fileName);
            file.transferTo(destFile);

            // 生成访问URL（相对路径）
            String relativePath = directory.replace(FileUploadConfig.UPLOAD_DIR, "");
            String fileUrl = "/uploads/" + relativePath.replace("\\", "/") + fileName;

            log.info("文件上传成功: {}", fileUrl);

            return new FileUploadVO(fileName, fileUrl, file.getSize());
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new BusinessException(500, "文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 删除文件
     */
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }

        try {
            // 移除URL前缀，构建完整路径
            String relativePath = fileUrl.replace("/uploads/", "");
            File file = new File(FileUploadConfig.UPLOAD_DIR + relativePath);

            if (file.exists()) {
                file.delete();
                log.info("文件删除成功: {}", fileUrl);
            }
        } catch (Exception e) {
            log.error("文件删除失败: {}", fileUrl, e);
        }
    }
}
