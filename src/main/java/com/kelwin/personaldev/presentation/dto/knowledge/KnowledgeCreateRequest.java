package com.kelwin.personaldev.presentation.dto.knowledge;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record KnowledgeCreateRequest(

        @NotNull 
        UUID userId,
        
        @NotBlank
        @Size(max = 255)
        String title,

        String description
) {
}