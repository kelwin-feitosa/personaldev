package com.kelwin.personaldev.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kelwin.personaldev.domain.model.Goal;

public interface GoalRepository extends JpaRepository<Goal, UUID> {
    List<Goal> findAllByUserId(UUID userId);
    List<Goal> findAllByParentGoalId(UUID parentGoalId);
}