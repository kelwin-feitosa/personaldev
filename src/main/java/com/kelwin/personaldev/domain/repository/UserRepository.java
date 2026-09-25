package com.kelwin.personaldev.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kelwin.personaldev.domain.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
}