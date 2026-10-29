package com.example.labres.controller;

import com.example.labres.common.ApiResponse;
import com.example.labres.domain.Notification;
import com.example.labres.domain.User;
import com.example.labres.mapper.NotificationMapper;
import com.example.labres.mapper.UserMapper;
import com.example.labres.security.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationMapper notificationMapper;
    private final UserMapper userMapper;

    public NotificationController(NotificationMapper notificationMapper, UserMapper userMapper) {
        this.notificationMapper = notificationMapper;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ApiResponse<List<Notification>> list() {
        User user = userMapper.findByUsername(SecurityUtils.currentUsername());
        return ApiResponse.ok(notificationMapper.findByUser(user.getId()));
    }

    @PatchMapping("/{id}/read")
    public ApiResponse<Void> read(@PathVariable Long id) {
        User user = userMapper.findByUsername(SecurityUtils.currentUsername());
        notificationMapper.markRead(id, user.getId());
        return ApiResponse.ok(null);
    }
}
