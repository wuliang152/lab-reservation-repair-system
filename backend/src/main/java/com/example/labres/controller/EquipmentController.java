package com.example.labres.controller;

import com.example.labres.common.ApiResponse;
import com.example.labres.domain.Equipment;
import com.example.labres.dto.EquipmentRequest;
import com.example.labres.service.EquipmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public ApiResponse<List<Map<String, Object>>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long labId,
            @RequestParam(required = false) String status) {
        if (keyword != null && keyword.isBlank()) keyword = null;
        if (status != null && status.isBlank()) status = null;
        return ApiResponse.ok(equipmentService.list(keyword, labId, status));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('LAB_ADMIN','SYS_ADMIN')")
    public ApiResponse<Equipment> create(@RequestBody EquipmentRequest request) {
        return ApiResponse.ok(equipmentService.create(request));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('LAB_ADMIN','SYS_ADMIN')")
    public ApiResponse<Equipment> update(@PathVariable Long id, @RequestBody EquipmentRequest request) {
        return ApiResponse.ok(equipmentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('LAB_ADMIN','SYS_ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        equipmentService.delete(id);
        return ApiResponse.ok(null);
    }
}
