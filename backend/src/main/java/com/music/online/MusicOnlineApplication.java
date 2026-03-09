package com.music.online;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 在线音乐播放器系统 - 启动类
 *
 * @author Claude
 * @since 2026-03-09
 */
@SpringBootApplication
public class MusicOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicOnlineApplication.class, args);
        System.out.println("========================================");
        System.out.println("在线音乐播放器系统启动成功！");
        System.out.println("接口文档地址: http://localhost:8080/api/doc.html");
        System.out.println("========================================");
    }
}
