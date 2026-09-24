package com.kelwin.personaldev.presentation.dto.activity;

import java.time.LocalDateTime;

public record ActivityResponse(
    Long id,
    Long userId,
    Long goalId,
    String title,
    String description,
    Integer estimatedDuration,
    Integer difficulty,
    Integer priority,
    boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}
