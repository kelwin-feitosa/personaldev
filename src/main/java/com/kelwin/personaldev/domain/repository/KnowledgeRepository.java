package com.kelwin.personaldev.domain.repository;

import com.kelwin.personaldev.domain.model.Knowledge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface KnowledgeRepository extends JpaRepository<Knowledge, UUID> {
    
}