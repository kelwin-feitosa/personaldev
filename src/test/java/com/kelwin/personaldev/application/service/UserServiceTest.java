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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final UUID USER_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUser() {
        UserCreateRequest request = createUserRequest();
        User user = createUser();

        when(repository.existsByEmail(request.email()))
                .thenReturn(false);

        when(repository.save(any(User.class)))
                .thenReturn(user);

        UserResponse response = userService.create(request);

        assertNotNull(response);
        assertEquals(USER_ID, response.id());
        assertEquals("Kelwin", response.name());
        assertEquals("kelwin@example.com", response.email());

        verify(repository).save(any(User.class));
    }

    @Test
    void shouldNotCreateUserWhenEmailAlreadyExists() {
        UserCreateRequest request = createUserRequest();

        when(repository.existsByEmail(request.email()))
                .thenReturn(true);

        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> userService.create(request)
        );

        verify(repository, never()).save(any(User.class));
    }

    @Test
    void shouldReturnAllUsers() {
        User user = createUser();

        when(repository.findAll())
                .thenReturn(List.of(user));

        List<UserResponse> response = userService.findAll();

        assertEquals(1, response.size());
        assertEquals(user.getId(), response.getFirst().id());
        assertEquals(user.getName(), response.getFirst().name());
        assertEquals(user.getEmail(), response.getFirst().email());
    }

    @Test
    void shouldReturnUserById() {
        User user = createUser();

        when(repository.findById(USER_ID))
                .thenReturn(Optional.of(user));

        UserResponse response = userService.findById(USER_ID);

        assertEquals(user.getId(), response.id());
        assertEquals(user.getName(), response.name());
        assertEquals(user.getEmail(), response.email());
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        when(repository.findById(USER_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.findById(USER_ID)
        );
    }

    @Test
    void shouldUpdateUser() {
        User user = createUser();

        UserCreateRequest request = new UserCreateRequest(
                "Novo nome",
                "novo@example.com"
        );

        when(repository.findById(USER_ID))
                .thenReturn(Optional.of(user));

        when(repository.existsByEmail(request.email()))
                .thenReturn(false);

        when(repository.save(user))
                .thenReturn(user);

        UserResponse response = userService.update(USER_ID, request);

        assertNotNull(response);
        assertEquals(USER_ID, response.id());
        assertEquals("Novo nome", response.name());
        assertEquals("novo@example.com", response.email());

        verify(repository).findById(USER_ID);
        verify(repository).existsByEmail("novo@example.com");
        verify(repository).save(user);
    }

    @Test
    void shouldUpdateUserWithoutCheckingEmailWhenEmailIsUnchanged() {
        User user = createUser();

        UserCreateRequest request = new UserCreateRequest(
                "Novo nome",
                "kelwin@example.com"
        );

        when(repository.findById(USER_ID))
                .thenReturn(Optional.of(user));

        when(repository.save(user))
                .thenReturn(user);

        UserResponse response = userService.update(USER_ID, request);

        assertNotNull(response);
        assertEquals(USER_ID, response.id());
        assertEquals("Novo nome", response.name());
        assertEquals("kelwin@example.com", response.email());

        verify(repository).findById(USER_ID);
        verify(repository, never()).existsByEmail(anyString());
        verify(repository).save(user);
    }

    @Test
    void shouldNotUpdateUserWhenEmailAlreadyExists() {
        User user = createUser();

        UserCreateRequest request = new UserCreateRequest(
                "Novo nome",
                "other@example.com"
        );

        when(repository.findById(USER_ID))
                .thenReturn(Optional.of(user));

        when(repository.existsByEmail(request.email()))
                .thenReturn(true);

        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> userService.update(USER_ID, request)
        );

        verify(repository).findById(USER_ID);
        verify(repository).existsByEmail(request.email());
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void shouldNotUpdateUserWhenUserDoesNotExist() {
        UserCreateRequest request = new UserCreateRequest(
                "Novo nome",
                "novo@example.com"
        );

        when(repository.findById(USER_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.update(USER_ID, request)
        );

        verify(repository).findById(USER_ID);
        verify(repository, never()).existsByEmail(anyString());
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void shouldDeleteUser() {
        User user = createUser();

        when(repository.findById(USER_ID))
                .thenReturn(Optional.of(user));

        userService.delete(USER_ID);

        verify(repository).findById(USER_ID);
        verify(repository).delete(user);
    }

    @Test
    void shouldNotDeleteUserWhenUserDoesNotExist() {
        when(repository.findById(USER_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.delete(USER_ID)
        );

        verify(repository).findById(USER_ID);
        verify(repository, never()).delete(any(User.class));
    }

    private UserCreateRequest createUserRequest() {
        return new UserCreateRequest(
                "Kelwin",
                "kelwin@example.com"
        );
    }

    private User createUser() {
        return User.builder()
                .id(USER_ID)
                .name("Kelwin")
                .email("kelwin@example.com")
                .build();
    }
}