package com.kelwin.personaldev.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kelwin.personaldev.domain.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}