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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final GoalRepository goalRepository;

    public ActivityResponse create(ActivityCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Goal goal = null;

        if (request.goalId() != null) {
            goal = goalRepository.findById(request.goalId())
                    .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));
        }

        Activity activity = Activity.builder()
                .user(user)
                .goal(goal)
                .title(request.title())
                .description(request.description())
                .estimatedDuration(request.estimatedDuration())
                .difficulty(request.difficulty())
                .priority(request.priority())
                .build();

        Activity savedActivity = activityRepository.save(activity);

        return toResponse(savedActivity);
    }

    public List<ActivityResponse> findAll() {
        return activityRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ActivityResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    public ActivityResponse update(UUID id, ActivityCreateRequest request) {
        Activity activity = findEntityById(id);

        Goal goal = null;

        if (request.goalId() != null) {
            goal = goalRepository.findById(request.goalId())
                    .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));
        }

        activity.update(
                request.title(),
                request.description(),
                request.estimatedDuration(),
                request.difficulty(),
                request.priority(),
                goal
        );

        Activity updatedActivity = activityRepository.save(activity);

        return toResponse(updatedActivity);
    }

    public void delete(UUID id) {
        Activity activity = findEntityById(id);

        activityRepository.delete(activity);
    }

    private Activity findEntityById(UUID id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found"));
    }

    private ActivityResponse toResponse(Activity activity) {
        return new ActivityResponse(
                activity.getId(),
                activity.getUser().getId(),
                activity.getGoal() != null ? activity.getGoal().getId() : null,
                activity.getTitle(),
                activity.getDescription(),
                activity.getEstimatedDuration(),
                activity.getDifficulty(),
                activity.getPriority(),
                activity.isActive(),
                activity.getCreatedAt(),
                activity.getUpdatedAt()
        );
    }
}