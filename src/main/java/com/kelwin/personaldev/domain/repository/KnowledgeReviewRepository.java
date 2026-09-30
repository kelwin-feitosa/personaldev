package com.kelwin.personaldev.domain.repository;

import com.kelwin.personaldev.domain.model.KnowledgeReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface KnowledgeReviewRepository extends JpaRepository<KnowledgeReview, UUID> {
    
}