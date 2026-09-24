package com.kelwin.personaldev.presentation.controller;

import com.kelwin.personaldev.application.service.ActivityService;
import com.kelwin.personaldev.presentation.dto.activity.ActivityResponse;
import com.kelwin.personaldev.presentation.exception.GlobalExceptionHandler;
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

@WebMvcTest(ActivityController.class)
@Import(GlobalExceptionHandler.class)
class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ActivityService activityService;

    @Test
    void shouldCreateActivity() throws Exception {

        ActivityResponse response = createActivityResponse();

        when(activityService.create(any()))
                .thenReturn(response);

        mockMvc.perform(post("/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Estudar Collections",
                                    "description": "Revisar List, Set e Map",
                                    "estimatedDuration": 60,
                                    "difficulty": 3,
                                    "priority": 1,
                                    "goalId": 1,
                                    "userId": 1
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.goalId").value(1))
                .andExpect(jsonPath("$.title").value("Estudar Collections"))
                .andExpect(jsonPath("$.description").value("Revisar List, Set e Map"))
                .andExpect(jsonPath("$.estimatedDuration").value(60))
                .andExpect(jsonPath("$.difficulty").value(3))
                .andExpect(jsonPath("$.priority").value(1))
                .andExpect(jsonPath("$.active").value(true));

        verify(activityService).create(any());
    }

    @Test
    void shouldReturnAllActivities() throws Exception {

        ActivityResponse activity1 = createActivityResponse();

        ActivityResponse activity2 = new ActivityResponse(
                2L,
                1L,
                null,
                "Estudar Spring",
                "Revisar Spring Boot",
                90,
                4,
                2,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(activityService.findAll())
                .thenReturn(List.of(activity1, activity2));

        mockMvc.perform(get("/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Estudar Collections"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Estudar Spring"));
    }

    @Test
    void shouldReturnActivityById() throws Exception {

        ActivityResponse response = createActivityResponse();

        when(activityService.findById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/activities/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.goalId").value(1))
                .andExpect(jsonPath("$.title").value("Estudar Collections"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldReturnNotFoundWhenActivityDoesNotExist() throws Exception {

        when(activityService.findById(999L))
                .thenThrow(
                        new ResourceNotFoundException("Activity not found")
                );

        mockMvc.perform(get("/activities/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Activity not found"));
    }

    @Test
    void shouldReturnBadRequestWhenRequestIsInvalid() throws Exception {

        mockMvc.perform(post("/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "",
                                    "estimatedDuration": -10,
                                    "difficulty": -1,
                                    "priority": -1,
                                    "userId": null
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(activityService, never()).create(any());
    }

    private ActivityResponse createActivityResponse() {

        return new ActivityResponse(
                1L,
                1L,
                1L,
                "Estudar Collections",
                "Revisar List, Set e Map",
                60,
                3,
                1,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}