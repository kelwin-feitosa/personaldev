package com.kelwin.personaldev.presentation.controller;

import com.kelwin.personaldev.application.service.UserService;
import com.kelwin.personaldev.presentation.dto.user.UserResponse;
import com.kelwin.personaldev.presentation.exception.GlobalExceptionHandler;
import com.kelwin.personaldev.presentation.exception.ResourceAlreadyExistsException;
import com.kelwin.personaldev.presentation.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    private static final UUID USER_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");

    private static final UUID SECOND_USER_ID =
            UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void shouldCreateUser() throws Exception {

        UserResponse response = createUserResponse();

        when(userService.create(any()))
                .thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Kelwin",
                                    "email": "kelwin@email.com"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(USER_ID.toString()))
                .andExpect(jsonPath("$.name").value("Kelwin"))
                .andExpect(jsonPath("$.email").value("kelwin@email.com"));

        verify(userService).create(any());
    }

    @Test
    void shouldReturnAllUsers() throws Exception {

        UserResponse user1 = createUserResponse();

        UserResponse user2 = new UserResponse(
                SECOND_USER_ID,
                "João",
                "joao@email.com",
                LocalDateTime.now()
        );

        when(userService.findAll())
                .thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(USER_ID.toString()))
                .andExpect(jsonPath("$[0].name").value("Kelwin"))
                .andExpect(jsonPath("$[1].id").value(SECOND_USER_ID.toString()))
                .andExpect(jsonPath("$[1].name").value("João"));
    }

    @Test
    void shouldReturnUserById() throws Exception {

        UserResponse response = createUserResponse();

        when(userService.findById(USER_ID))
                .thenReturn(response);

        mockMvc.perform(get("/users/" + USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID.toString()))
                .andExpect(jsonPath("$.name").value("Kelwin"))
                .andExpect(jsonPath("$.email").value("kelwin@email.com"));
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {

        UUID nonExistentUserId =
                UUID.fromString("99999999-9999-9999-9999-999999999999");

        when(userService.findById(nonExistentUserId))
                .thenThrow(
                        new ResourceNotFoundException("User not found")
                );

        mockMvc.perform(get("/users/" + nonExistentUserId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {

        when(userService.create(any()))
                .thenThrow(
                        new ResourceAlreadyExistsException(
                                "Email already registered"
                        )
                );

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Kelwin",
                                    "email": "kelwin@email.com"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(content().string("Email already registered"));
    }

    @Test
    void shouldReturnBadRequestWhenRequestIsInvalid() throws Exception {

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "",
                                    "email": "email-invalido"
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(userService, never()).create(any());
    }

    private UserResponse createUserResponse() {

        return new UserResponse(
                USER_ID,
                "Kelwin",
                "kelwin@email.com",
                LocalDateTime.now()
        );
    }
}