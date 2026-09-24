package com.kelwin.personaldev.application.service;

import com.kelwin.personaldev.domain.model.Activity;
import com.kelwin.personaldev.domain.model.Goal;
import com.kelwin.personaldev.domain.model.User;
import com.kelwin.personaldev.domain.repository.ActivityRepository;
import com.kelwin.personaldev.domain.repository.GoalRepository;
import com.kelwin.personaldev.domain.repository.UserRepository;
import com.kelwin.personaldev.presentation.dto.activity.ActivityCreateRequest;
import com.kelwin.personaldev.presentation.dto.activity.ActivityResponse;
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
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private ActivityService activityService;

    @Test
    void shouldCreateActivityWithoutGoal() {
        User user = createUser();
        ActivityCreateRequest request = createActivityRequestWithoutGoal();
        Activity activity = createActivity(user, null);

        when(userRepository.findById(request.userId()))
                .thenReturn(Optional.of(user));

        when(activityRepository.save(any(Activity.class)))
                .thenReturn(activity);

        ActivityResponse response = activityService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(1L, response.userId());
        assertNull(response.goalId());
        assertEquals("Estudar Collections", response.title());
        assertEquals(60, response.estimatedDuration());
        assertTrue(response.active());

        verify(activityRepository).save(any(Activity.class));
    }

    @Test
    void shouldCreateActivityWithGoal() {
        User user = createUser();
        Goal goal = createGoal(user);
        ActivityCreateRequest request = createActivityRequestWithGoal();
        Activity activity = createActivity(user, goal);

        when(userRepository.findById(request.userId()))
                .thenReturn(Optional.of(user));

        when(goalRepository.findById(request.goalId()))
                .thenReturn(Optional.of(goal));

        when(activityRepository.save(any(Activity.class)))
                .thenReturn(activity);

        ActivityResponse response = activityService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(1L, response.userId());
        assertEquals(1L, response.goalId());
        assertEquals("Estudar Collections", response.title());
    }

    @Test
    void shouldNotCreateActivityWhenUserDoesNotExist() {
        ActivityCreateRequest request = createActivityRequestWithoutGoal();

        when(userRepository.findById(request.userId()))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> activityService.create(request)
        );

        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    void shouldNotCreateActivityWhenGoalDoesNotExist() {
        User user = createUser();
        ActivityCreateRequest request = createActivityRequestWithGoal();

        when(userRepository.findById(request.userId()))
                .thenReturn(Optional.of(user));

        when(goalRepository.findById(request.goalId()))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> activityService.create(request)
        );

        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    void shouldReturnAllActivities() {
        User user = createUser();
        Activity activity = createActivity(user, null);

        when(activityRepository.findAll())
                .thenReturn(List.of(activity));

        List<ActivityResponse> response = activityService.findAll();

        assertEquals(1, response.size());
        assertEquals(activity.getId(), response.getFirst().id());
        assertEquals(activity.getTitle(), response.getFirst().title());
        assertEquals(activity.getUser().getId(), response.getFirst().userId());
        assertNull(response.getFirst().goalId());
    }

    @Test
    void shouldReturnActivityById() {
        User user = createUser();
        Activity activity = createActivity(user, null);

        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));

        ActivityResponse response = activityService.findById(1L);

        assertEquals(activity.getId(), response.id());
        assertEquals(activity.getTitle(), response.title());
        assertEquals(activity.getUser().getId(), response.userId());
        assertNull(response.goalId());
    }

    @Test
    void shouldThrowExceptionWhenActivityDoesNotExist() {
        when(activityRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> activityService.findById(1L)
        );
    }

    private User createUser() {
        return User.builder()
                .id(1L)
                .name("Kelwin")
                .email("kelwin@example.com")
                .build();
    }

    private Goal createGoal(User user) {
        return Goal.builder()
                .id(1L)
                .user(user)
                .title("Aprender Java")
                .build();
    }

    private Activity createActivity(User user, Goal goal) {
        return Activity.builder()
                .id(1L)
                .user(user)
                .goal(goal)
                .title("Estudar Collections")
                .description("Revisar List, Set e Map")
                .estimatedDuration(60)
                .difficulty(3)
                .priority(1)
                .active(true)
                .build();
    }

    private ActivityCreateRequest createActivityRequestWithoutGoal() {
        return new ActivityCreateRequest(
                "Estudar Collections",
                "Revisar List, Set e Map",
                60,
                3,
                1,
                null,
                1L
        );
    }

    private ActivityCreateRequest createActivityRequestWithGoal() {
        return new ActivityCreateRequest(
                "Estudar Collections",
                "Revisar List, Set e Map",
                60,
                3,
                1,
                1L,
                1L
        );
    }
}