package com.example.labres.service;

import com.example.labres.common.BusinessException;
import com.example.labres.common.Constants;
import com.example.labres.domain.User;
import com.example.labres.dto.LoginRequest;
import com.example.labres.dto.LoginResponse;
import com.example.labres.mapper.UserMapper;
import com.example.labres.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userMapper.findByUsername(request.username());
        if (user == null || !passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException(401, "账号或密码错误");
        }
        if (!Constants.USER_ACTIVE.equals(user.getStatus())) {
            throw new BusinessException(401, "账号已停用");
        }
        String token = tokenProvider.generateToken(user.getUsername(), user.getRole());
        return new LoginResponse(token, userToMap(user));
    }

    public Map<String, Object> me(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new BusinessException(401, "用户不存在");
        }
        return userToMap(user);
    }

    public static Map<String, Object> userToMap(User user) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("name", user.getName());
        map.put("role", user.getRole());
        map.put("status", user.getStatus());
        map.put("createdAt", user.getCreatedAt());
        return map;
    }
}
