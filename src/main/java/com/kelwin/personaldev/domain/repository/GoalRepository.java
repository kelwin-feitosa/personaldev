package com.kelwin.personaldev.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kelwin.personaldev.domain.model.Goal;

public interface GoalRepository extends JpaRepository<Goal, UUID> {

}