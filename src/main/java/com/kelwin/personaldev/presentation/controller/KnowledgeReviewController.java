package com.kelwin.personaldev.presentation.controller;

import com.kelwin.personaldev.application.service.KnowledgeReviewService;
import com.kelwin.personaldev.presentation.dto.knowledgereview.KnowledgeReviewCreateRequest;
import com.kelwin.personaldev.presentation.dto.knowledgereview.KnowledgeReviewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController 
@RequestMapping("/knowledge")
@RequiredArgsConstructor
@Tag(
        name = "Knowledge Reviews",
        description = "Endpoints for managing knowledge reviews"
)
public class KnowledgeReviewController {

    private final KnowledgeReviewService service;

    @PostMapping("/{knowledgeId}/reviews")
    @Operation(
            summary = "Create knowledge review",
            description = "Creates a review for a knowledge item"
    )
    public ResponseEntity<KnowledgeReviewResponse> create(
            @PathVariable UUID knowledgeId,
            @Valid @RequestBody KnowledgeReviewCreateRequest request
    ) {
        return ResponseEntity.ok(
                service.create(knowledgeId, request)
        );
    }

    @GetMapping("/reviews")
    @Operation(
            summary = "List knowledge reviews",
            description = "Returns all knowledge reviews"
    )
    public ResponseEntity<List<KnowledgeReviewResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/reviews/{id}")
    @Operation(
            summary = "Find knowledge review by ID",
            description = "Returns a knowledge review by its ID"
    )
    public ResponseEntity<KnowledgeReviewResponse> findById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/reviews/{id}")
    @Operation(
            summary = "Delete knowledge review",
            description = "Deletes a knowledge review by its ID"
    )
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}