package com.kelwin.personaldev.application.service;

import com.kelwin.personaldev.domain.model.Activity;
import com.kelwin.personaldev.domain.model.ActivityExecution;
import com.kelwin.personaldev.domain.model.enums.ActivityStatus;
import com.kelwin.personaldev.domain.repository.ActivityExecutionRepository;
import com.kelwin.personaldev.domain.repository.ActivityRepository;
import com.kelwin.personaldev.presentation.dto.activityexecution.ActivityExecutionResponse;
import com.kelwin.personaldev.presentation.exception.BusinessRuleException;
import com.kelwin.personaldev.presentation.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivityExecutionService {

    private final ActivityExecutionRepository executionRepository;
    private final ActivityRepository activityRepository;

    public ActivityExecutionResponse start(UUID activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found"));

        if (activity.getStatus() != ActivityStatus.IN_PROGRESS) {
            throw new BusinessRuleException(
                    "Only in-progress activities can have executions"
            );
        }

        if (executionRepository.existsByActivityIdAndEndedAtIsNull(activityId)) {
            throw new BusinessRuleException(
                    "Activity already has an active execution"
            );
        }

        ActivityExecution execution = ActivityExecution.builder()
            .activity(activity)
            .startedAt(LocalDateTime.now())
            .build();
        
        ActivityExecution savedExecution = executionRepository.save(execution);

        return toResponse(savedExecution);
    }

    public ActivityExecutionResponse finish(UUID executionId) {
        ActivityExecution execution = findEntityById(executionId);

        execution.finish();

        ActivityExecution savedExecution = executionRepository.save(execution);

        return toResponse(savedExecution);
    }

    public List<ActivityExecutionResponse> findAll() {
        return executionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ActivityExecutionResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    public void delete(UUID id) {
        ActivityExecution execution = findEntityById(id);
        executionRepository.delete(execution);
    }

    private ActivityExecution findEntityById(UUID id) {
        return executionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Activity execution not found"));
    }

    private ActivityExecutionResponse toResponse(ActivityExecution execution) {
        return new ActivityExecutionResponse(
                execution.getId(),
                execution.getActivity().getId(),
                execution.getStartedAt(),
                execution.getEndedAt(),
                execution.getActualDuration()
        );
    }
}