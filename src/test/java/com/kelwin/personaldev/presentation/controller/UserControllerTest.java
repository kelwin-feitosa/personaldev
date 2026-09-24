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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

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
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Kelwin"))
                .andExpect(jsonPath("$.email").value("kelwin@email.com"));

        verify(userService).create(any());
    }

    @Test
    void shouldReturnAllUsers() throws Exception {

        UserResponse user1 = createUserResponse();

        UserResponse user2 = new UserResponse(
                2L,
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
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Kelwin"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("João"));
    }

    @Test
    void shouldReturnUserById() throws Exception {

        UserResponse response = createUserResponse();

        when(userService.findById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Kelwin"))
                .andExpect(jsonPath("$.email").value("kelwin@email.com"));
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {

        when(userService.findById(999L))
                .thenThrow(
                        new ResourceNotFoundException("User not found")
                );

        mockMvc.perform(get("/users/999"))
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
                1L,
                "Kelwin",
                "kelwin@email.com",
                LocalDateTime.now()
        );
    }
}