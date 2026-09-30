package com.kelwin.personaldev.presentation.dto.activityexecution;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityExecutionResponse(
        UUID id,
        UUID activityId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        Integer actualDuration
) {
}