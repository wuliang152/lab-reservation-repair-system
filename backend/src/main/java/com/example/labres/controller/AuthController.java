package com.example.labres.controller;

import com.example.labres.common.ApiResponse;
import com.example.labres.dto.LoginRequest;
import com.example.labres.dto.LoginResponse;
import com.example.labres.security.SecurityUtils;
import com.example.labres.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        return ApiResponse.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> me() {
        return ApiResponse.ok(authService.me(SecurityUtils.currentUsername()));
    }
}
