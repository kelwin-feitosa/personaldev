package com.kelwin.personaldev.presentation.dto.goal;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.kelwin.personaldev.domain.model.GoalStatus;

public record GoalResponse(
    Long id,
    Long userId,
    String title,
    String description,
    GoalStatus status,
    Integer priority,
    LocalDate deadline,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}
