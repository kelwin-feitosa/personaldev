
package com.kelwin.personaldev.application.service;

import com.kelwin.personaldev.domain.model.Goal;
import com.kelwin.personaldev.domain.model.User;
import com.kelwin.personaldev.domain.model.enums.GoalDeadlineType;
import com.kelwin.personaldev.domain.model.enums.GoalStatus;
import com.kelwin.personaldev.domain.repository.GoalRepository;
import com.kelwin.personaldev.domain.repository.UserRepository;
import com.kelwin.personaldev.presentation.dto.goal.GoalCreateRequest;
import com.kelwin.personaldev.presentation.dto.goal.GoalResponse;
import com.kelwin.personaldev.presentation.exception.BusinessRuleException;
import com.kelwin.personaldev.presentation.exception.ResourceNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoalServiceTest {

    private static final UUID USER_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");

    private static final UUID GOAL_ID =
            UUID.fromString("22222222-2222-2222-2222-222222222222");

    private static final UUID CHILD_GOAL_ID =
            UUID.fromString("33333333-3333-3333-3333-333333333333");

    private static final UUID OTHER_USER_ID =
            UUID.fromString("44444444-4444-4444-4444-444444444444");

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

        when(userRepository.findById(USER_ID))
                .thenReturn(Optional.of(user));

        when(goalRepository.save(any(Goal.class)))
                .thenReturn(goal);

        GoalResponse response = goalService.create(request);

        assertNotNull(response);
        assertEquals(GOAL_ID, response.id());
        assertEquals(USER_ID, response.userId());
        assertEquals("Aprender Java", response.title());
        assertEquals(GoalStatus.ACTIVE, response.status());
        assertEquals(1, response.priority());
        assertEquals(GoalDeadlineType.FLEXIBLE, response.deadlineType());
        assertNull(response.parentGoalId());
        assertTrue(response.childGoalIds().isEmpty());

        verify(goalRepository).save(any(Goal.class));
    }

    @Test
    void shouldNotCreateGoalWhenUserDoesNotExist() {
        GoalCreateRequest request = createGoalRequest();

        when(userRepository.findById(USER_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> goalService.create(request)
        );

        verify(goalRepository, never()).save(any(Goal.class));
    }

    @Test
    void shouldCreateGoalWithParent() {
        User user = createUser();
        Goal parent = createGoal(user);

        GoalCreateRequest request = new GoalCreateRequest(
                "Aprender Spring Boot",
                "Criar APIs REST",
                GoalStatus.ACTIVE,
                2,
                LocalDate.of(2026, 12, 1),
                GoalDeadlineType.FIXED,
                GOAL_ID,
                USER_ID
        );

        Goal child = Goal.builder()
                .id(CHILD_GOAL_ID)
                .user(user)
                .title("Aprender Spring Boot")
                .description("Criar APIs REST")
                .status(GoalStatus.ACTIVE)
                .priority(2)
                .deadline(LocalDate.of(2026, 12, 1))
                .deadlineType(GoalDeadlineType.FIXED)
                .parentGoal(parent)
                .build();

        when(userRepository.findById(USER_ID))
                .thenReturn(Optional.of(user));

        when(goalRepository.findById(GOAL_ID))
                .thenReturn(Optional.of(parent));

        when(goalRepository.save(any(Goal.class)))
                .thenReturn(child);

        GoalResponse response = goalService.create(request);

        assertEquals(CHILD_GOAL_ID, response.id());
        assertEquals(GOAL_ID, response.parentGoalId());
        assertEquals(GoalDeadlineType.FIXED, response.deadlineType());
    }

    @Test
    void shouldRejectParentGoalFromAnotherUser() {
        User user = createUser();

        User otherUser = User.builder()
                .id(OTHER_USER_ID)
                .name("Outro usuário")
                .email("outro@example.com")
                .build();

        Goal parent = createGoal(otherUser);

        GoalCreateRequest request = new GoalCreateRequest(
                "Aprender Spring Boot",
                "Criar APIs REST",
                GoalStatus.ACTIVE,
                2,
                null,
                GoalDeadlineType.FLEXIBLE,
                GOAL_ID,
                USER_ID
        );

        when(userRepository.findById(USER_ID))
                .thenReturn(Optional.of(user));

        when(goalRepository.findById(GOAL_ID))
                .thenReturn(Optional.of(parent));

        assertThrows(
                BusinessRuleException.class,
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
        assertEquals(GoalDeadlineType.FLEXIBLE,
                response.getFirst().deadlineType());
        assertNull(response.getFirst().parentGoalId());
        assertTrue(response.getFirst().childGoalIds().isEmpty());
    }

    @Test
    void shouldReturnGoalById() {
        User user = createUser();
        Goal goal = createGoal(user);

        when(goalRepository.findById(GOAL_ID))
                .thenReturn(Optional.of(goal));

        GoalResponse response = goalService.findById(GOAL_ID);

        assertEquals(goal.getId(), response.id());
        assertEquals(goal.getTitle(), response.title());
        assertEquals(goal.getUser().getId(), response.userId());
        assertEquals(GoalDeadlineType.FLEXIBLE, response.deadlineType());
    }

    @Test
    void shouldThrowExceptionWhenGoalDoesNotExist() {
        when(goalRepository.findById(GOAL_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> goalService.findById(GOAL_ID)
        );
    }

    @Test
    void shouldUpdateGoal() {
        User user = createUser();
        Goal goal = createGoal(user);

        GoalCreateRequest request = new GoalCreateRequest(
                "Novo objetivo",
                "Nova descrição",
                GoalStatus.COMPLETED,
                5,
                LocalDate.of(2026, 12, 1),
                GoalDeadlineType.FIXED,
                null,
                USER_ID
        );

        when(goalRepository.findById(GOAL_ID))
                .thenReturn(Optional.of(goal));

        when(goalRepository.save(goal))
                .thenReturn(goal);

        GoalResponse response = goalService.update(GOAL_ID, request);

        assertNotNull(response);
        assertEquals(GOAL_ID, response.id());
        assertEquals(USER_ID, response.userId());
        assertEquals("Novo objetivo", response.title());
        assertEquals("Nova descrição", response.description());
        assertEquals(GoalStatus.COMPLETED, response.status());
        assertEquals(5, response.priority());
        assertEquals(LocalDate.of(2026, 12, 1), response.deadline());
        assertEquals(GoalDeadlineType.FIXED, response.deadlineType());
        assertNull(response.parentGoalId());

        verify(goalRepository).findById(GOAL_ID);
        verify(goalRepository).save(goal);
    }

    @Test
    void shouldRejectUpdateThatCreatesHierarchyCycle() {
        User user = createUser();

        Goal parent = createGoal(user);

        Goal child = Goal.builder()
                .id(CHILD_GOAL_ID)
                .user(user)
                .title("Objetivo filho")
                .description("Filho do objetivo principal")
                .status(GoalStatus.ACTIVE)
                .priority(2)
                .parentGoal(parent)
                .build();

        GoalCreateRequest request = new GoalCreateRequest(
                "Aprender Java",
                "Estudar Java avançado",
                GoalStatus.ACTIVE,
                1,
                null,
                GoalDeadlineType.FLEXIBLE,
                CHILD_GOAL_ID,
                USER_ID
        );

        when(goalRepository.findById(GOAL_ID))
                .thenReturn(Optional.of(parent));

        when(goalRepository.findById(CHILD_GOAL_ID))
                .thenReturn(Optional.of(child));

        assertThrows(
                BusinessRuleException.class,
                () -> goalService.update(GOAL_ID, request)
        );

        verify(goalRepository, never()).save(any(Goal.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingGoal() {
        GoalCreateRequest request = createGoalRequest();

        when(goalRepository.findById(GOAL_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> goalService.update(GOAL_ID, request)
        );

        verify(goalRepository).findById(GOAL_ID);
        verify(goalRepository, never()).save(any(Goal.class));
    }

    @Test
    void shouldDeleteGoalWithoutChildren() {
        User user = createUser();
        Goal goal = createGoal(user);

        when(goalRepository.findById(GOAL_ID))
                .thenReturn(Optional.of(goal));

        goalService.delete(GOAL_ID);

        verify(goalRepository).findById(GOAL_ID);
        verify(goalRepository).delete(goal);
    }

    @Test
    void shouldRejectDeletingGoalWithChildren() {
        User user = createUser();

        Goal child = Goal.builder()
                .id(CHILD_GOAL_ID)
                .user(user)
                .title("Objetivo filho")
                .description("Filho do objetivo principal")
                .status(GoalStatus.ACTIVE)
                .priority(2)
                .build();

        Goal parent = Goal.builder()
                .id(GOAL_ID)
                .user(user)
                .title("Objetivo principal")
                .description("Agrupa outros objetivos")
                .status(GoalStatus.ACTIVE)
                .priority(1)
                .childGoals(List.of(child))
                .build();

        when(goalRepository.findById(GOAL_ID))
                .thenReturn(Optional.of(parent));

        assertThrows(
                BusinessRuleException.class,
                () -> goalService.delete(GOAL_ID)
        );

        verify(goalRepository, never()).delete(any(Goal.class));
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingGoal() {
        when(goalRepository.findById(GOAL_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> goalService.delete(GOAL_ID)
        );

        verify(goalRepository).findById(GOAL_ID);
        verify(goalRepository, never()).delete(any(Goal.class));
    }

    private User createUser() {
        return User.builder()
                .id(USER_ID)
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
                null,
                null,
                USER_ID
        );
    }

    private Goal createGoal(User user) {
        return Goal.builder()
                .id(GOAL_ID)
                .user(user)
                .title("Aprender Java")
                .description("Estudar Java avançado")
                .status(GoalStatus.ACTIVE)
                .priority(1)
                .build();
    }
}