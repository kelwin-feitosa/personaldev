package com.kelwin.personaldev.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kelwin.personaldev.domain.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {

}