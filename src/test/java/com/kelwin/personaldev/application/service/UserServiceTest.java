package com.kelwin.personaldev.application.service;

import com.kelwin.personaldev.domain.model.User;
import com.kelwin.personaldev.domain.repository.UserRepository;
import com.kelwin.personaldev.presentation.dto.user.UserCreateRequest;
import com.kelwin.personaldev.presentation.dto.user.UserResponse;
import com.kelwin.personaldev.presentation.exception.ResourceAlreadyExistsException;
import com.kelwin.personaldev.presentation.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUser() {
        UserCreateRequest request = createUserRequest();
        User user = createUser();

        when(repository.existsByEmail(request.email())).thenReturn(false);
        when(repository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Kelwin", response.name());
        assertEquals("kelwin@example.com", response.email());

        verify(repository).save(any(User.class));
    }

    @Test
    void shouldNotCreateUserWhenEmailAlreadyExists() {
        UserCreateRequest request = createUserRequest();

        when(repository.existsByEmail(request.email())).thenReturn(true);

        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> userService.create(request)
        );

        verify(repository, never()).save(any(User.class));
    }

    @Test
    void shouldReturnAllUsers() {
        User user = createUser();

        when(repository.findAll()).thenReturn(List.of(user));

        List<UserResponse> response = userService.findAll();

        assertEquals(1, response.size());
        assertEquals(user.getId(), response.getFirst().id());
        assertEquals(user.getName(), response.getFirst().name());
        assertEquals(user.getEmail(), response.getFirst().email());
    }

    @Test
    void shouldReturnUserById() {
        User user = createUser();

        when(repository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = userService.findById(1L);

        assertEquals(user.getId(), response.id());
        assertEquals(user.getName(), response.name());
        assertEquals(user.getEmail(), response.email());
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.findById(1L)
        );
    }

    private UserCreateRequest createUserRequest() {
        return new UserCreateRequest(
                "Kelwin",
                "kelwin@example.com"
        );
    }

    private User createUser() {
        return User.builder()
                .id(1L)
                .name("Kelwin")
                .email("kelwin@example.com")
                .build();
    }
}