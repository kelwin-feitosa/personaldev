package com.kelwin.personaldev.presentation.dto.activity;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityResponse(
    UUID id,
    UUID userId,
    UUID goalId,
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
