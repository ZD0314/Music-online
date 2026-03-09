package com.music.online.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * 文件上传配置
 *
 * @since 2026-03-09
 */
@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

    /**
     * 文件上传根目录（使用绝对路径）
     */
    public static final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;

    /**
     * 图片上传目录
     */
    public static final String IMAGE_DIR = UPLOAD_DIR + "images" + File.separator;

    /**
     * 音频上传目录
     */
    public static final String AUDIO_DIR = UPLOAD_DIR + "audio" + File.separator;

    /**
     * 歌词上传目录
     */
    public static final String LYRIC_DIR = UPLOAD_DIR + "lyrics" + File.separator;

    /**
     * 配置静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + UPLOAD_DIR);
    }
}
