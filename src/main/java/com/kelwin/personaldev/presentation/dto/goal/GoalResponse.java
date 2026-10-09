package com.kelwin.personaldev.presentation.dto.goal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.kelwin.personaldev.domain.model.enums.GoalDeadlineType;
import com.kelwin.personaldev.domain.model.enums.GoalStatus;

public record GoalResponse(

        UUID id,
        UUID userId,
        String title,
        String description,
        GoalStatus status,
        Integer priority,
        LocalDate deadline,
        GoalDeadlineType deadlineType,
        UUID parentGoalId,
        List<UUID> childGoalIds,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}