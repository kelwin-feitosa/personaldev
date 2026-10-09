package com.kelwin.personaldev.application.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.kelwin.personaldev.domain.model.enums.GoalDeadlineType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kelwin.personaldev.domain.model.Goal;
import com.kelwin.personaldev.domain.model.User;
import com.kelwin.personaldev.domain.repository.GoalRepository;
import com.kelwin.personaldev.domain.repository.UserRepository;
import com.kelwin.personaldev.presentation.dto.goal.GoalCreateRequest;
import com.kelwin.personaldev.presentation.dto.goal.GoalResponse;
import com.kelwin.personaldev.presentation.exception.BusinessRuleException;
import com.kelwin.personaldev.presentation.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    public GoalResponse create(GoalCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Goal parentGoal = resolveParentGoal(
                request.parentGoalId(),
                user.getId(),
                null
        );

        GoalDeadlineType deadlineType = request.deadlineType() != null
                ? request.deadlineType()
                : GoalDeadlineType.FLEXIBLE;

        Goal goal = Goal.builder()
                .user(user)
                .title(request.title())
                .description(request.description())
                .status(request.status())
                .priority(request.priority())
                .deadline(request.deadline())
                .deadlineType(deadlineType)
                .parentGoal(parentGoal)
                .build();

        return toResponse(goalRepository.save(goal));
    }

    @Transactional(readOnly = true)
    public List<GoalResponse> findAll() {
        return goalRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public GoalResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    public GoalResponse update(UUID id, GoalCreateRequest request) {
        Goal goal = findEntityById(id);

        Goal parentGoal = resolveParentGoal(
            request.parentGoalId(),
            goal.getUser().getId(),
            goal.getId()
        );

        validateNoCycle(goal, parentGoal);

        GoalDeadlineType deadlineType = request.deadlineType() != null
                ? request.deadlineType()
                : GoalDeadlineType.FLEXIBLE;

        goal.update(
            request.title(),
            request.description(),
            request.status(),
            request.priority(),
            request.deadline(),
            deadlineType,
            parentGoal
        );

        return toResponse(goalRepository.save(goal));
    }

    public void delete(UUID id) {
        Goal goal = findEntityById(id);

        if (!goal.getChildGoals().isEmpty()) {
            throw new BusinessRuleException(
                    "Cannot delete a goal that has child goals");
        }

        goalRepository.delete(goal);
    }

    private Goal resolveParentGoal(
            UUID parentGoalId,
            UUID userId,
            UUID currentGoalId
    ) {
        if (parentGoalId == null) {
            return null;
        }

        if (parentGoalId.equals(currentGoalId)) {
            throw new BusinessRuleException(
                    "A goal cannot be its own parent");
        }

        Goal parentGoal = findEntityById(parentGoalId);

        if (!parentGoal.getUser().getId().equals(userId)) {
            throw new BusinessRuleException(
                    "Parent goal must belong to the same user");
        }

        return parentGoal;
    }

    private void validateNoCycle(Goal goal, Goal proposedParent) {
        Set<UUID> visited = new HashSet<>();
        Goal current = proposedParent;

        while (current != null) {
            if (current.getId().equals(goal.getId())) {
                throw new BusinessRuleException(
                        "Goal hierarchy cannot contain cycles");
            }

            if (!visited.add(current.getId())) {
                throw new BusinessRuleException(
                        "Existing goal hierarchy contains a cycle");
            }

            current = current.getParentGoal();
        }
    }

    private Goal findEntityById(UUID id) {
        return goalRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Goal not found"));
    }

    private GoalResponse toResponse(Goal goal) {
        List<UUID> childGoalIds = goal.getChildGoals().stream()
                .map(Goal::getId)
                .toList();

        UUID parentGoalId = goal.getParentGoal() != null
                ? goal.getParentGoal().getId()
                : null;

        return new GoalResponse(
                goal.getId(),
                goal.getUser().getId(),
                goal.getTitle(),
                goal.getDescription(),
                goal.getStatus(),
                goal.getPriority(),
                goal.getDeadline(),
                goal.getDeadlineType(),
                parentGoalId,
                childGoalIds,
                goal.getCreatedAt(),
                goal.getUpdatedAt()
        );
    }
}