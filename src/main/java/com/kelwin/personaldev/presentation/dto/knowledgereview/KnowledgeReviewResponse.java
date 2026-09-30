package com.kelwin.personaldev.presentation.dto.knowledgereview;

import com.kelwin.personaldev.domain.model.enums.ReviewPerformance;

import java.time.LocalDateTime;
import java.util.UUID;

public record KnowledgeReviewResponse(
        UUID id,
        UUID knowledgeId,
        LocalDateTime reviewedAt,
        ReviewPerformance performance
) {
}