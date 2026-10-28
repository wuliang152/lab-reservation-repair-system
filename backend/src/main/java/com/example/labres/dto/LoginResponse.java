package com.example.labres.dto;

import java.util.Map;

public record LoginResponse(String token, Map<String, Object> user) {}
