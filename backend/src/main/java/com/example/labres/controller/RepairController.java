package com.example.labres.controller;

import com.example.labres.common.ApiResponse;
import com.example.labres.domain.RepairOrder;
import com.example.labres.dto.AssignRequest;
import com.example.labres.dto.CompleteRequest;
import com.example.labres.dto.RepairRequest;
import com.example.labres.service.RepairService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/repairs")
public class RepairController {

    private final RepairService repairService;

    public RepairController(RepairService repairService) {
        this.repairService = repairService;
    }

    @GetMapping
    public ApiResponse<List<Map<String, Object>>> list() {
        return ApiResponse.ok(repairService.list());
    }

    @PostMapping
    public ApiResponse<RepairOrder> create(@RequestBody RepairRequest request) {
        return ApiResponse.ok(repairService.create(request));
    }

    @PostMapping("/{id}/assign")
    @PreAuthorize("hasAnyRole('LAB_ADMIN','SYS_ADMIN')")
    public ApiResponse<Void> assign(@PathVariable Long id, @RequestBody AssignRequest request) {
        repairService.assign(id, request.technicianId());
        return ApiResponse.ok(null);
    }

    @PostMapping("/{id}/accept")
    @PreAuthorize("hasRole('TECHNICIAN')")
    public ApiResponse<Void> accept(@PathVariable Long id) {
        repairService.accept(id);
        return ApiResponse.ok(null);
    }

    @PostMapping("/{id}/start")
    @PreAuthorize("hasRole('TECHNICIAN')")
    public ApiResponse<Void> start(@PathVariable Long id) {
        repairService.start(id);
        return ApiResponse.ok(null);
    }

    @PostMapping("/{id}/complete")
    @PreAuthorize("hasRole('TECHNICIAN')")
    public ApiResponse<Void> complete(@PathVariable Long id, @RequestBody CompleteRequest request) {
        repairService.complete(id, request.result());
        return ApiResponse.ok(null);
    }

    @PostMapping("/{id}/confirm")
    public ApiResponse<Void> confirm(@PathVariable Long id) {
        repairService.confirm(id);
        return ApiResponse.ok(null);
    }
}
