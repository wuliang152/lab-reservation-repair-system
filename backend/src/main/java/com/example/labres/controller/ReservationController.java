package com.example.labres.controller;

import com.example.labres.common.ApiResponse;
import com.example.labres.domain.Reservation;
import com.example.labres.dto.RejectRequest;
import com.example.labres.dto.ReservationRequest;
import com.example.labres.service.ReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ApiResponse<List<Map<String, Object>>> list() {
        return ApiResponse.ok(reservationService.list());
    }

    @GetMapping("/availability")
    public ApiResponse<List<Map<String, Object>>> availability(
            @RequestParam(required = false) Long equipmentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ApiResponse.ok(reservationService.availability(equipmentId, start, end));
    }

    @PostMapping
    public ApiResponse<Reservation> create(@RequestBody ReservationRequest request) {
        return ApiResponse.ok(reservationService.create(request));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('LAB_ADMIN','SYS_ADMIN')")
    public ApiResponse<Void> approve(@PathVariable Long id) {
        reservationService.approve(id);
        return ApiResponse.ok(null);
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('LAB_ADMIN','SYS_ADMIN')")
    public ApiResponse<Void> reject(@PathVariable Long id, @RequestBody RejectRequest request) {
        reservationService.reject(id, request.reason());
        return ApiResponse.ok(null);
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancel(@PathVariable Long id) {
        reservationService.cancel(id);
        return ApiResponse.ok(null);
    }
}
