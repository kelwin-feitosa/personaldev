package com.kelwin.personaldev.presentation.dto.goal;

import java.time.LocalDate;
import java.util.UUID;

import com.kelwin.personaldev.domain.model.GoalStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record GoalCreateRequest(
    @NotBlank
    String title,

    String description,

    @NotNull
    GoalStatus status,

    @NotNull
    @Positive
    Integer priority,

    LocalDate deadline,

    @NotNull
    UUID userId
) {

}
