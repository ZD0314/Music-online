package com.music.online.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.music.online.common.BusinessException;
import com.music.online.dto.UpdatePasswordDTO;
import com.music.online.dto.UpdateUserInfoDTO;
import com.music.online.dto.UserLoginDTO;
import com.music.online.dto.UserRegisterDTO;
import com.music.online.entity.User;
import com.music.online.mapper.UserMapper;
import com.music.online.util.JwtUtil;
import com.music.online.util.PasswordUtil;
import com.music.online.vo.LoginVO;
import com.music.online.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务
 *
 * @author Claude
 * @since 2026-03-09
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    /**
     * 用户注册
     */
    public LoginVO register(UserRegisterDTO dto) {
        // 检查用户名是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(2001, "用户名已存在");
        }

        // 检查手机号是否存在
        if (dto.getPhone() != null && !dto.getPhone().isEmpty()) {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone, dto.getPhone());
            if (userMapper.selectCount(wrapper) > 0) {
                throw new BusinessException(2002, "手机号已被注册");
            }
        }

        // 检查邮箱是否存在
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getEmail, dto.getEmail());
            if (userMapper.selectCount(wrapper) > 0) {
                throw new BusinessException(2003, "邮箱已被注册");
            }
        }

        // 创建用户
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(PasswordUtil.encode(dto.getPassword()));
        user.setNickname(dto.getNickname() != null ? dto.getNickname() : dto.getUsername());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setStatus(1);

        userMapper.insert(user);

        // 注册成功后自动登录，生成 Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), "user");

        // 构建用户信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("nickname", user.getNickname());
        userInfo.put("phone", user.getPhone());
        userInfo.put("email", user.getEmail());
        userInfo.put("avatar", user.getAvatar());
        userInfo.put("signature", user.getSignature());
        userInfo.put("userType", "user");

        return new LoginVO(token, userInfo);
    }

    /**
     * 用户登录
     */
    public LoginVO login(UserLoginDTO dto) {
        // 查询用户（支持用户名/手机号/邮箱登录）
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.eq(User::getUsername, dto.getAccount())
                .or().eq(User::getPhone, dto.getAccount())
                .or().eq(User::getEmail, dto.getAccount()));
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            throw new BusinessException(1001, "账号或密码错误");
        }

        // 验证密码
        if (!PasswordUtil.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(1001, "账号或密码错误");
        }

        // 检查状态
        if (user.getStatus() == 0) {
            throw new BusinessException(1002, "账号已被禁用");
        }

        // 生成 Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), "user");

        // 构建用户信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("nickname", user.getNickname());
        userInfo.put("phone", user.getPhone());
        userInfo.put("email", user.getEmail());
        userInfo.put("avatar", user.getAvatar());
        userInfo.put("signature", user.getSignature());
        userInfo.put("userType", "user");

        return new LoginVO(token, userInfo);
    }

    /**
     * 根据ID获取用户信息
     */
    public UserVO getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(1003, "用户不存在");
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 更新用户信息
     */
    public void updateUserInfo(Long userId, UpdateUserInfoDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(1003, "用户不存在");
        }

        // 检查手机号是否被其他用户使用
        if (dto.getPhone() != null && !dto.getPhone().isEmpty() && !dto.getPhone().equals(user.getPhone())) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone, dto.getPhone()).ne(User::getId, userId);
            if (userMapper.selectCount(wrapper) > 0) {
                throw new BusinessException(2002, "手机号已被其他用户使用");
            }
        }

        // 检查邮箱是否被其他用户使用
        if (dto.getEmail() != null && !dto.getEmail().isEmpty() && !dto.getEmail().equals(user.getEmail())) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getEmail, dto.getEmail()).ne(User::getId, userId);
            if (userMapper.selectCount(wrapper) > 0) {
                throw new BusinessException(2003, "邮箱已被其他用户使用");
            }
        }

        // 更新用户信息
        if (dto.getNickname() != null) user.setNickname(dto.getNickname());
        if (dto.getGender() != null) user.setGender(dto.getGender());
        if (dto.getBirthday() != null) user.setBirthday(dto.getBirthday());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getAvatar() != null) user.setAvatar(dto.getAvatar());
        if (dto.getBio() != null) user.setSignature(dto.getBio());

        userMapper.updateById(user);
    }

    /**
     * 修改密码
     */
    public void updatePassword(Long userId, UpdatePasswordDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(1003, "用户不存在");
        }

        // 验证旧密码
        if (!PasswordUtil.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BusinessException(2004, "旧密码错误");
        }

        // 更新密码
        user.setPassword(PasswordUtil.encode(dto.getNewPassword()));
        userMapper.updateById(user);
    }
}
