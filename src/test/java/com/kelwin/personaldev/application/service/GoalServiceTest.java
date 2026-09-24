package com.kelwin.personaldev.application.service;

import com.kelwin.personaldev.domain.model.Goal;
import com.kelwin.personaldev.domain.model.GoalStatus;
import com.kelwin.personaldev.domain.model.User;
import com.kelwin.personaldev.domain.repository.GoalRepository;
import com.kelwin.personaldev.domain.repository.UserRepository;
import com.kelwin.personaldev.presentation.dto.goal.GoalCreateRequest;
import com.kelwin.personaldev.presentation.dto.goal.GoalResponse;
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
class GoalServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GoalService goalService;

    @Test
    void shouldCreateGoal() {
        User user = createUser();
        GoalCreateRequest request = createGoalRequest();
        Goal goal = createGoal(user);

        when(userRepository.findById(request.userId()))
                .thenReturn(Optional.of(user));

        when(goalRepository.save(any(Goal.class)))
                .thenReturn(goal);

        GoalResponse response = goalService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(1L, response.userId());
        assertEquals("Aprender Java", response.title());
        assertEquals(GoalStatus.ACTIVE, response.status());
        assertEquals(1, response.priority());

        verify(goalRepository).save(any(Goal.class));
    }

    @Test
    void shouldNotCreateGoalWhenUserDoesNotExist() {
        GoalCreateRequest request = createGoalRequest();

        when(userRepository.findById(request.userId()))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> goalService.create(request)
        );

        verify(goalRepository, never()).save(any(Goal.class));
    }

    @Test
    void shouldReturnAllGoals() {
        User user = createUser();
        Goal goal = createGoal(user);

        when(goalRepository.findAll())
                .thenReturn(List.of(goal));

        List<GoalResponse> response = goalService.findAll();

        assertEquals(1, response.size());
        assertEquals(goal.getId(), response.getFirst().id());
        assertEquals(goal.getTitle(), response.getFirst().title());
        assertEquals(goal.getUser().getId(), response.getFirst().userId());
    }

    @Test
    void shouldReturnGoalById() {
        User user = createUser();
        Goal goal = createGoal(user);

        when(goalRepository.findById(1L))
                .thenReturn(Optional.of(goal));

        GoalResponse response = goalService.findById(1L);

        assertEquals(goal.getId(), response.id());
        assertEquals(goal.getTitle(), response.title());
        assertEquals(goal.getUser().getId(), response.userId());
    }

    @Test
    void shouldThrowExceptionWhenGoalDoesNotExist() {
        when(goalRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> goalService.findById(1L)
        );
    }

    private User createUser() {
        return User.builder()
                .id(1L)
                .name("Kelwin")
                .email("kelwin@example.com")
                .build();
    }

    private GoalCreateRequest createGoalRequest() {
        return new GoalCreateRequest(
                "Aprender Java",
                "Estudar Java avançado",
                GoalStatus.ACTIVE,
                1,
                null,
                1L
        );
    }

    private Goal createGoal(User user) {
        return Goal.builder()
                .id(1L)
                .user(user)
                .title("Aprender Java")
                .description("Estudar Java avançado")
                .status(GoalStatus.ACTIVE)
                .priority(1)
                .build();
    }
}