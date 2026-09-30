package com.kelwin.personaldev.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kelwin.personaldev.domain.model.ActivityExecution;

public interface ActivityExecutionRepository extends JpaRepository<ActivityExecution, UUID>{
    boolean existsByActivityIdAndEndedAtIsNull(UUID activityId);
}
