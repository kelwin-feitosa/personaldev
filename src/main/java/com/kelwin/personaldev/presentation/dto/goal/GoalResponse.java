package com.kelwin.personaldev.presentation.dto.goal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.kelwin.personaldev.domain.model.GoalStatus;

public record GoalResponse(
    UUID id,
    UUID userId,
    String title,
    String description,
    GoalStatus status,
    Integer priority,
    LocalDate deadline,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}
