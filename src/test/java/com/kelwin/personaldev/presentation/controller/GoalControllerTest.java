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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GoalController.class)
@Import(GlobalExceptionHandler.class)
class GoalControllerTest {

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
                                    "userId": 1
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
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
                2L,
                1L,
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
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Aprender Java"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Aprender Python"));
    }

    @Test
    void shouldReturnGoalById() throws Exception {

        GoalResponse response = createGoalResponse();

        when(goalService.findById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/goals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.title").value("Aprender Java"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldReturnNotFoundWhenGoalDoesNotExist() throws Exception {

        when(goalService.findById(999L))
                .thenThrow(
                        new ResourceNotFoundException("Goal not found")
                );

        mockMvc.perform(get("/goals/999"))
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
                1L,
                1L,
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