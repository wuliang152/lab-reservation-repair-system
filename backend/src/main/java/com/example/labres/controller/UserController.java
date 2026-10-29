package com.example.labres.controller;

import com.example.labres.common.ApiResponse;
import com.example.labres.dto.UserStatusRequest;
import com.example.labres.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SYS_ADMIN','LAB_ADMIN')")
    public ApiResponse<List<Map<String, Object>>> list() {
        return ApiResponse.ok(userService.list());
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('SYS_ADMIN')")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @RequestBody UserStatusRequest request) {
        userService.updateStatus(id, request.status());
        return ApiResponse.ok(null);
    }
}
