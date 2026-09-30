package com.kelwin.personaldev.presentation.controller;

import com.kelwin.personaldev.application.service.KnowledgeService;
import com.kelwin.personaldev.presentation.dto.knowledge.KnowledgeCreateRequest;
import com.kelwin.personaldev.presentation.dto.knowledge.KnowledgeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/knowledge")
@RequiredArgsConstructor
@Tag(
        name = "Knowledge",
        description = "Endpoints for managing knowledge"
)
public class KnowledgeController {

    private final KnowledgeService service;

    @PostMapping
    @Operation(
            summary = "Create knowledge",
            description = "Creates a new knowledge item for a user"
    )
    public ResponseEntity<KnowledgeResponse> create(
            @Valid @RequestBody KnowledgeCreateRequest request
    ) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping
    @Operation(
            summary = "List knowledge",
            description = "Returns all knowledge items"
    )
    public ResponseEntity<List<KnowledgeResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Find knowledge by ID",
            description = "Returns a knowledge item by its ID"
    )
    public ResponseEntity<KnowledgeResponse> findById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update knowledge",
            description = "Updates the title and description of a knowledge item"
    )
    public ResponseEntity<KnowledgeResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody KnowledgeCreateRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete knowledge",
            description = "Deletes a knowledge item by its ID"
    )
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}