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
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));

        return toResponse(goal);
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