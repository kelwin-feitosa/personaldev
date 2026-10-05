package com.kelwin.personaldev.application.service;

import com.kelwin.personaldev.domain.model.Knowledge;
import com.kelwin.personaldev.domain.model.KnowledgeReview;
import com.kelwin.personaldev.domain.repository.KnowledgeRepository;
import com.kelwin.personaldev.domain.repository.KnowledgeReviewRepository;
import com.kelwin.personaldev.presentation.dto.knowledgereview.KnowledgeReviewCreateRequest;
import com.kelwin.personaldev.presentation.dto.knowledgereview.KnowledgeReviewResponse;
import com.kelwin.personaldev.presentation.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KnowledgeReviewService {

    private final KnowledgeReviewRepository reviewRepository;
    private final KnowledgeRepository knowledgeRepository;

    public KnowledgeReviewResponse create(
            UUID knowledgeId,
            KnowledgeReviewCreateRequest request
    ) {
        Knowledge knowledge = knowledgeRepository.findById(knowledgeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Knowledge not found"));

        KnowledgeReview review = KnowledgeReview.builder()
            .knowledge(knowledge)
            .reviewedAt(LocalDateTime.now())
            .performance(request.performance())
            .build();

        knowledge.registerReview(
                request.performance(),
                review.getReviewedAt()
        );

        knowledgeRepository.save(knowledge);

        KnowledgeReview savedReview = reviewRepository.save(review);

        return toResponse(savedReview);
    }

    public List<KnowledgeReviewResponse> findAll() {
        return reviewRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public KnowledgeReviewResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    public void delete(UUID id) {
        KnowledgeReview review = findEntityById(id);
        reviewRepository.delete(review);
    }

    private KnowledgeReview findEntityById(UUID id) {
        return reviewRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Knowledge review not found"));
    }

    private KnowledgeReviewResponse toResponse(KnowledgeReview review) {
        return new KnowledgeReviewResponse(
                review.getId(),
                review.getKnowledge().getId(),
                review.getReviewedAt(),
                review.getPerformance()
        );
    }
}
