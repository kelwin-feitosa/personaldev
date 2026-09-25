package com.kelwin.personaldev.presentation.controller;

import com.kelwin.personaldev.application.service.GoalService;
import com.kelwin.personaldev.domain.model.GoalStatus;
import com.kelwin.personaldev.presentation.dto.goal.GoalResponse;
import com.kelwin.personaldev.presentation.exception.GlobalExceptionHandler;
import com.kelwin.personaldev.presentation.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GoalController.class)
@Import(GlobalExceptionHandler.class)
class GoalControllerTest {

    private static final UUID USER_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");

    private static final UUID GOAL_ID =
            UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GoalService goalService;

    @Test
    void shouldCreateGoal() throws Exception {

        GoalResponse response = createGoalResponse();

        when(goalService.create(any()))
                .thenReturn(response);

        mockMvc.perform(post("/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Aprender Java",
                                    "description": "Estudar Java e Spring",
                                    "status": "ACTIVE",
                                    "priority": 1,
                                    "deadline": "2026-12-31",
                                    "userId": "11111111-1111-1111-1111-111111111111"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(GOAL_ID.toString()))
                .andExpect(jsonPath("$.userId").value(USER_ID.toString()))
                .andExpect(jsonPath("$.title").value("Aprender Java"))
                .andExpect(jsonPath("$.description").value("Estudar Java e Spring"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.priority").value(1));

        verify(goalService).create(any());
    }

    @Test
    void shouldReturnAllGoals() throws Exception {

        GoalResponse goal1 = createGoalResponse();

        GoalResponse goal2 = new GoalResponse(
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                USER_ID,
                "Aprender Python",
                "Estudar Python",
                GoalStatus.ACTIVE,
                2,
                LocalDate.of(2026, 12, 31),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(goalService.findAll())
                .thenReturn(List.of(goal1, goal2));

        mockMvc.perform(get("/goals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(GOAL_ID.toString()))
                .andExpect(jsonPath("$[0].title").value("Aprender Java"))
                .andExpect(jsonPath("$[1].id").value("33333333-3333-3333-3333-333333333333"))
                .andExpect(jsonPath("$[1].title").value("Aprender Python"));
    }

    @Test
    void shouldReturnGoalById() throws Exception {

        GoalResponse response = createGoalResponse();

        when(goalService.findById(GOAL_ID))
                .thenReturn(response);

        mockMvc.perform(get("/goals/" + GOAL_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(GOAL_ID.toString()))
                .andExpect(jsonPath("$.userId").value(USER_ID.toString()))
                .andExpect(jsonPath("$.title").value("Aprender Java"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldReturnNotFoundWhenGoalDoesNotExist() throws Exception {

        UUID nonExistentGoalId =
                UUID.fromString("99999999-9999-9999-9999-999999999999");

        when(goalService.findById(nonExistentGoalId))
                .thenThrow(
                        new ResourceNotFoundException("Goal not found")
                );

        mockMvc.perform(get("/goals/" + nonExistentGoalId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Goal not found"));
    }

    @Test
    void shouldReturnBadRequestWhenRequestIsInvalid() throws Exception {

        mockMvc.perform(post("/goals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "",
                                    "status": "ACTIVE",
                                    "priority": -1,
                                    "userId": null
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(goalService, never()).create(any());
    }

    private GoalResponse createGoalResponse() {

        return new GoalResponse(
                GOAL_ID,
                USER_ID,
                "Aprender Java",
                "Estudar Java e Spring",
                GoalStatus.ACTIVE,
                1,
                LocalDate.of(2026, 12, 31),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}