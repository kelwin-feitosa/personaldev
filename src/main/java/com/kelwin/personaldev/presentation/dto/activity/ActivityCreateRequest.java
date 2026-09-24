package com.kelwin.personaldev.presentation.dto.activity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ActivityCreateRequest(
    @NotBlank
    String title,

    String description,

    @Positive
    Integer estimatedDuration,

    @Positive
    Integer difficulty,

    @Positive
    Integer priority,

    Long goalId,

    @NotNull
    Long userId
) {

}
