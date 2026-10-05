package com.kelwin.personaldev.presentation.dto.knowledge;

import java.time.LocalDateTime;
import java.util.UUID;

public record KnowledgeResponse(
        UUID id,
        UUID userId,
        String title,
        String description,
        LocalDateTime createdAt,
        LocalDateTime nextReviewAt,
        Integer reviewIntervalDays
) {
}