package com.kelwin.personaldev.presentation.dto.user;

import java.time.LocalDateTime;

public record UserResponse(
    Long id,
    String name,
    String email,
    LocalDateTime createdAt
) {

}
