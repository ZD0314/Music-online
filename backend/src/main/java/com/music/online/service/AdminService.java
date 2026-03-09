package com.music.online.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.music.online.common.BusinessException;
import com.music.online.dto.AdminLoginDTO;
import com.music.online.entity.*;
import com.music.online.mapper.*;
import com.music.online.util.JwtUtil;
import com.music.online.util.PasswordUtil;
import com.music.online.vo.LoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理员服务
 *
 * @author Claude
 * @since 2026-03-09
 */
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminMapper adminMapper;
    private final UserMapper userMapper;
    private final ArtistMapper artistMapper;
    private final SongMapper songMapper;
    private final AlbumMapper albumMapper;
    private final PlaylistMapper playlistMapper;
    private final JwtUtil jwtUtil;

    /**
     * 管理员登录
     */
    public LoginVO login(AdminLoginDTO dto) {
        // 查询管理员
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, dto.getUsername());
        Admin admin = adminMapper.selectOne(wrapper);

        if (admin == null) {
            throw new BusinessException(1001, "用户名或密码错误");
        }

        // 验证密码
        if (!PasswordUtil.matches(dto.getPassword(), admin.getPassword())) {
            throw new BusinessException(1001, "用户名或密码错误");
        }

        // 检查状态
        if (admin.getStatus() == 0) {
            throw new BusinessException(1002, "账号已被禁用");
        }

        // 生成 Token
        String token = jwtUtil.generateToken(admin.getId(), admin.getUsername(), "admin");

        // 构建用户信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", admin.getId());
        userInfo.put("username", admin.getUsername());
        userInfo.put("nickname", admin.getNickname());
        userInfo.put("userType", "admin");

        return new LoginVO(token, userInfo);
    }

    /**
     * 获取统计数据
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 用户总数
        Long userCount = userMapper.selectCount(null);
        statistics.put("userCount", userCount);

        // 歌手总数
        Long artistCount = artistMapper.selectCount(null);
        statistics.put("artistCount", artistCount);

        // 歌曲总数
        Long songCount = songMapper.selectCount(null);
        statistics.put("songCount", songCount);

        // 专辑总数
        Long albumCount = albumMapper.selectCount(null);
        statistics.put("albumCount", albumCount);

        // 歌单总数
        Long playlistCount = playlistMapper.selectCount(null);
        statistics.put("playlistCount", playlistCount);

        // 活跃用户数 (简化处理: 状态为正常的用户数)
        LambdaQueryWrapper<User> activeUserWrapper = new LambdaQueryWrapper<>();
        activeUserWrapper.eq(User::getStatus, 1);
        Long activeUserCount = userMapper.selectCount(activeUserWrapper);
        statistics.put("activeUserCount", activeUserCount);

        // 今日播放次数 (所有歌曲的播放次数总和)
        Long totalPlayCount = songMapper.selectList(null).stream()
                .mapToLong(song -> song.getPlayCount() != null ? song.getPlayCount() : 0)
                .sum();
        statistics.put("todayPlayCount", totalPlayCount);

        return statistics;
    }
}
