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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ActivityController.class)
@Import(GlobalExceptionHandler.class)
class ActivityControllerTest {

    private static final UUID USER_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");

    private static final UUID GOAL_ID =
            UUID.fromString("22222222-2222-2222-2222-222222222222");

    private static final UUID ACTIVITY_ID =
            UUID.fromString("33333333-3333-3333-3333-333333333333");

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
                                    "goalId": "22222222-2222-2222-2222-222222222222",
                                    "userId": "11111111-1111-1111-1111-111111111111"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ACTIVITY_ID.toString()))
                .andExpect(jsonPath("$.userId").value(USER_ID.toString()))
                .andExpect(jsonPath("$.goalId").value(GOAL_ID.toString()))
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
                UUID.fromString("44444444-4444-4444-4444-444444444444"),
                USER_ID,
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
                .andExpect(jsonPath("$[0].id").value(ACTIVITY_ID.toString()))
                .andExpect(jsonPath("$[0].title").value("Estudar Collections"))
                .andExpect(jsonPath("$[1].id").value("44444444-4444-4444-4444-444444444444"))
                .andExpect(jsonPath("$[1].title").value("Estudar Spring"));
    }

    @Test
    void shouldReturnActivityById() throws Exception {

        ActivityResponse response = createActivityResponse();

        when(activityService.findById(ACTIVITY_ID))
                .thenReturn(response);

        mockMvc.perform(get("/activities/" + ACTIVITY_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ACTIVITY_ID.toString()))
                .andExpect(jsonPath("$.userId").value(USER_ID.toString()))
                .andExpect(jsonPath("$.goalId").value(GOAL_ID.toString()))
                .andExpect(jsonPath("$.title").value("Estudar Collections"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldReturnNotFoundWhenActivityDoesNotExist() throws Exception {

        UUID nonExistentActivityId =
                UUID.fromString("99999999-9999-9999-9999-999999999999");

        when(activityService.findById(nonExistentActivityId))
                .thenThrow(
                        new ResourceNotFoundException("Activity not found")
                );

        mockMvc.perform(get("/activities/" + nonExistentActivityId))
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
                ACTIVITY_ID,
                USER_ID,
                GOAL_ID,
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