package com.kelwin.personaldev.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kelwin.personaldev.domain.model.User;
import com.kelwin.personaldev.domain.repository.UserRepository;
import com.kelwin.personaldev.presentation.dto.user.UserCreateRequest;
import com.kelwin.personaldev.presentation.dto.user.UserResponse;
import com.kelwin.personaldev.presentation.exception.ResourceAlreadyExistsException;
import com.kelwin.personaldev.presentation.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@Service 
public class UserService {

    private final UserRepository repository;

    public UserResponse create(UserCreateRequest request) {
        if(repository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("Email already registered");
        }

        User user = User.builder()
            .name(request.name())
            .email(request.email())
            .build();

        User savedUser = repository.save(user);

        return toResponse(savedUser);
    }

    public List<UserResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse findById(UUID id) {
        User user = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getCreatedAt()
        );
    }
}
