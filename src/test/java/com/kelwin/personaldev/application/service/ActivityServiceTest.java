package com.kelwin.personaldev.application.service;

import com.kelwin.personaldev.domain.model.Activity;
import com.kelwin.personaldev.domain.model.Goal;
import com.kelwin.personaldev.domain.model.GoalStatus;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    private static final UUID USER_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");

    private static final UUID GOAL_ID =
            UUID.fromString("22222222-2222-2222-2222-222222222222");

    private static final UUID ACTIVITY_ID =
            UUID.fromString("33333333-3333-3333-3333-333333333333");

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
        assertEquals(ACTIVITY_ID, response.id());
        assertEquals(USER_ID, response.userId());
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
        assertEquals(ACTIVITY_ID, response.id());
        assertEquals(USER_ID, response.userId());
        assertEquals(GOAL_ID, response.goalId());
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

        when(activityRepository.findById(ACTIVITY_ID))
                .thenReturn(Optional.of(activity));

        ActivityResponse response = activityService.findById(ACTIVITY_ID);

        assertEquals(activity.getId(), response.id());
        assertEquals(activity.getTitle(), response.title());
        assertEquals(activity.getUser().getId(), response.userId());
        assertNull(response.goalId());
    }

    @Test
    void shouldThrowExceptionWhenActivityDoesNotExist() {
        when(activityRepository.findById(ACTIVITY_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> activityService.findById(ACTIVITY_ID)
        );
    }

    @Test
    void shouldUpdateActivityWithoutGoal() {
        User user = createUser();
        Activity activity = createActivity(user, null);

        ActivityCreateRequest request = new ActivityCreateRequest(
                "Novo título",
                "Nova descrição",
                90,
                4,
                2,
                null,
                USER_ID
        );

        when(activityRepository.findById(ACTIVITY_ID))
                .thenReturn(Optional.of(activity));

        when(activityRepository.save(activity))
                .thenReturn(activity);

        ActivityResponse response = activityService.update(
                ACTIVITY_ID,
                request
        );

        assertNotNull(response);
        assertEquals(ACTIVITY_ID, response.id());
        assertEquals(USER_ID, response.userId());
        assertNull(response.goalId());
        assertEquals("Novo título", response.title());
        assertEquals("Nova descrição", response.description());
        assertEquals(90, response.estimatedDuration());
        assertEquals(4, response.difficulty());
        assertEquals(2, response.priority());

        verify(activityRepository).findById(ACTIVITY_ID);
        verify(activityRepository).save(activity);
    }

    @Test
    void shouldUpdateActivityWithGoal() {
        User user = createUser();
        Goal oldGoal = createGoal(user);
        Goal newGoal = Goal.builder()
                .id(UUID.fromString("44444444-4444-4444-4444-444444444444"))
                .user(user)
                .title("Novo objetivo")
                .status(GoalStatus.ACTIVE)
                .priority(1)
                .build();

        Activity activity = createActivity(user, oldGoal);

        ActivityCreateRequest request = new ActivityCreateRequest(
                "Nova atividade",
                "Nova descrição",
                120,
                5,
                3,
                newGoal.getId(),
                USER_ID
        );

        when(activityRepository.findById(ACTIVITY_ID))
                .thenReturn(Optional.of(activity));

        when(goalRepository.findById(newGoal.getId()))
                .thenReturn(Optional.of(newGoal));

        when(activityRepository.save(activity))
                .thenReturn(activity);

        ActivityResponse response = activityService.update(
                ACTIVITY_ID,
                request
        );

        assertNotNull(response);
        assertEquals(ACTIVITY_ID, response.id());
        assertEquals(USER_ID, response.userId());
        assertEquals(newGoal.getId(), response.goalId());
        assertEquals("Nova atividade", response.title());
        assertEquals("Nova descrição", response.description());
        assertEquals(120, response.estimatedDuration());
        assertEquals(5, response.difficulty());
        assertEquals(3, response.priority());

        verify(activityRepository).findById(ACTIVITY_ID);
        verify(goalRepository).findById(newGoal.getId());
        verify(activityRepository).save(activity);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingActivity() {
        ActivityCreateRequest request = createActivityRequestWithoutGoal();

        when(activityRepository.findById(ACTIVITY_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> activityService.update(ACTIVITY_ID, request)
        );

        verify(activityRepository).findById(ACTIVITY_ID);
        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingActivityWithNonExistingGoal() {
        User user = createUser();
        Activity activity = createActivity(user, null);
        ActivityCreateRequest request = createActivityRequestWithGoal();

        when(activityRepository.findById(ACTIVITY_ID))
                .thenReturn(Optional.of(activity));

        when(goalRepository.findById(GOAL_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> activityService.update(ACTIVITY_ID, request)
        );

        verify(activityRepository).findById(ACTIVITY_ID);
        verify(goalRepository).findById(GOAL_ID);
        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    void shouldDeleteActivity() {
        User user = createUser();
        Activity activity = createActivity(user, null);

        when(activityRepository.findById(ACTIVITY_ID))
                .thenReturn(Optional.of(activity));

        activityService.delete(ACTIVITY_ID);

        verify(activityRepository).findById(ACTIVITY_ID);
        verify(activityRepository).delete(activity);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingActivity() {
        when(activityRepository.findById(ACTIVITY_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> activityService.delete(ACTIVITY_ID)
        );

        verify(activityRepository).findById(ACTIVITY_ID);
        verify(activityRepository, never()).delete(any(Activity.class));
    }

    private User createUser() {
        return User.builder()
                .id(USER_ID)
                .name("Kelwin")
                .email("kelwin@example.com")
                .build();
    }

    private Goal createGoal(User user) {
        return Goal.builder()
                .id(GOAL_ID)
                .user(user)
                .title("Aprender Java")
                .status(GoalStatus.ACTIVE)
                .priority(1)
                .build();
    }

    private Activity createActivity(User user, Goal goal) {
        return Activity.builder()
                .id(ACTIVITY_ID)
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
                USER_ID
        );
    }

    private ActivityCreateRequest createActivityRequestWithGoal() {
        return new ActivityCreateRequest(
                "Estudar Collections",
                "Revisar List, Set e Map",
                60,
                3,
                1,
                GOAL_ID,
                USER_ID
        );
    }
}