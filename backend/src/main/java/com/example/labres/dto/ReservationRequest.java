package com.example.labres.dto;

import java.time.LocalDateTime;

public record ReservationRequest(Long equipmentId, LocalDateTime startTime, LocalDateTime endTime, String purpose) {}
