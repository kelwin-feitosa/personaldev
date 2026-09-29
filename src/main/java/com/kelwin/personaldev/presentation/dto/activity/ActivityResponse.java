package com.kelwin.personaldev.presentation.dto.activity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.kelwin.personaldev.domain.model.enums.ActivityStatus;

public record ActivityResponse(
    UUID id,
    UUID userId,
    UUID goalId,
    String title,
    String description,
    Integer estimatedDuration,
    Integer difficulty,
    Integer priority,
    ActivityStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}
