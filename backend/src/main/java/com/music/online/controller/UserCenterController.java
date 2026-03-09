package com.music.online.controller;

import com.music.online.common.Result;
import com.music.online.dto.CreatePlaylistDTO;
import com.music.online.dto.UpdatePasswordDTO;
import com.music.online.dto.UpdateUserInfoDTO;
import com.music.online.entity.User;
import com.music.online.service.PlaylistService;
import com.music.online.service.UserService;
import com.music.online.util.JwtUtil;
import com.music.online.vo.PlaylistVO;
import com.music.online.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 用户个人中心控制器
 */
@Api(tags = "用户个人中心")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserCenterController {

    private final UserService userService;
    private final PlaylistService playlistService;
    private final JwtUtil jwtUtil;

    /**
     * 获取当前用户信息
     */
    @ApiOperation("获取当前用户信息")
    @GetMapping("/info")
    public Result<UserVO> getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtil.getUserIdFromToken(token);
        UserVO userVO = userService.getUserById(userId);
        return Result.success(userVO);
    }

    /**
     * 更新用户信息
     */
    @ApiOperation("更新用户信息")
    @PutMapping("/info")
    public Result<Void> updateUserInfo(@Valid @RequestBody UpdateUserInfoDTO dto, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtil.getUserIdFromToken(token);
        userService.updateUserInfo(userId, dto);
        return Result.success();
    }

    /**
     * 修改密码
     */
    @ApiOperation("修改密码")
    @PutMapping("/password")
    public Result<Void> updatePassword(@Valid @RequestBody UpdatePasswordDTO dto, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtil.getUserIdFromToken(token);
        userService.updatePassword(userId, dto);
        return Result.success();
    }

    /**
     * 创建个人歌单
     */
    @ApiOperation("创建个人歌单")
    @PostMapping("/playlists")
    public Result<Void> createPlaylist(@Valid @RequestBody CreatePlaylistDTO dto, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtil.getUserIdFromToken(token);
        playlistService.createUserPlaylist(userId, dto);
        return Result.success();
    }

    /**
     * 获取个人歌单列表
     */
    @ApiOperation("获取个人歌单列表")
    @GetMapping("/playlists")
    public Result<List<PlaylistVO>> getUserPlaylists(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtil.getUserIdFromToken(token);
        List<PlaylistVO> playlists = playlistService.getUserPlaylists(userId);
        return Result.success(playlists);
    }

    /**
     * 删除个人歌单
     */
    @ApiOperation("删除个人歌单")
    @DeleteMapping("/playlists/{id}")
    public Result<Void> deletePlaylist(@ApiParam("歌单ID") @PathVariable Long id, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtil.getUserIdFromToken(token);
        playlistService.deleteUserPlaylist(userId, id);
        return Result.success();
    }

    /**
     * 更新个人歌单
     */
    @ApiOperation("更新个人歌单")
    @PutMapping("/playlists/{id}")
    public Result<Void> updatePlaylist(
            @ApiParam("歌单ID") @PathVariable Long id,
            @Valid @RequestBody CreatePlaylistDTO dto,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtil.getUserIdFromToken(token);
        playlistService.updateUserPlaylist(userId, id, dto);
        return Result.success();
    }
}
