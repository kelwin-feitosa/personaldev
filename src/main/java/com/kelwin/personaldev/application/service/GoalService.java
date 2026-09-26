package com.kelwin.personaldev.application.service;

import com.kelwin.personaldev.domain.model.Goal;
import com.kelwin.personaldev.domain.model.User;
import com.kelwin.personaldev.domain.repository.GoalRepository;
import com.kelwin.personaldev.domain.repository.UserRepository;
import com.kelwin.personaldev.presentation.dto.goal.GoalCreateRequest;
import com.kelwin.personaldev.presentation.dto.goal.GoalResponse;
import com.kelwin.personaldev.presentation.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    public GoalResponse create(GoalCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Goal goal = Goal.builder()
                .user(user)
                .title(request.title())
                .description(request.description())
                .status(request.status())
                .priority(request.priority())
                .deadline(request.deadline())
                .build();

        Goal savedGoal = goalRepository.save(goal);

        return toResponse(savedGoal);
    }

    public List<GoalResponse> findAll() {
        return goalRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public GoalResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    public GoalResponse update(UUID id, GoalCreateRequest request) {
        Goal goal = findEntityById(id);

        goal.update(
                request.title(),
                request.description(),
                request.status(),
                request.priority(),
                request.deadline()
        );

        Goal updatedGoal = goalRepository.save(goal);

        return toResponse(updatedGoal);
    }

    public void delete(UUID id) {
        Goal goal = findEntityById(id);

        goalRepository.delete(goal);
    }

    private Goal findEntityById(UUID id) {
        return goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));
    }

    private GoalResponse toResponse(Goal goal) {
        return new GoalResponse(
                goal.getId(),
                goal.getUser().getId(),
                goal.getTitle(),
                goal.getDescription(),
                goal.getStatus(),
                goal.getPriority(),
                goal.getDeadline(),
                goal.getCreatedAt(),
                goal.getUpdatedAt()
        );
    }
}