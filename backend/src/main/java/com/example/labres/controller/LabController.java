package com.example.labres.controller;

import com.example.labres.common.ApiResponse;
import com.example.labres.domain.Lab;
import com.example.labres.dto.LabRequest;
import com.example.labres.service.LabService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/labs")
public class LabController {

    private final LabService labService;

    public LabController(LabService labService) {
        this.labService = labService;
    }

    @GetMapping
    public ApiResponse<List<Lab>> list() {
        return ApiResponse.ok(labService.list());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('LAB_ADMIN','SYS_ADMIN')")
    public ApiResponse<Lab> create(@RequestBody LabRequest request) {
        return ApiResponse.ok(labService.create(request));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('LAB_ADMIN','SYS_ADMIN')")
    public ApiResponse<Lab> update(@PathVariable Long id, @RequestBody LabRequest request) {
        return ApiResponse.ok(labService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('LAB_ADMIN','SYS_ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        labService.delete(id);
        return ApiResponse.ok(null);
    }
}
