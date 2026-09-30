package com.kelwin.personaldev.presentation.dto.knowledgereview;

import com.kelwin.personaldev.domain.model.enums.ReviewPerformance;
import jakarta.validation.constraints.NotNull;

public record KnowledgeReviewCreateRequest(

        @NotNull
        ReviewPerformance performance

) {
}