package com.kelwin.personaldev.presentation.controller;

import com.kelwin.personaldev.application.service.GoalService;
import com.kelwin.personaldev.presentation.dto.goal.GoalCreateRequest;
import com.kelwin.personaldev.presentation.dto.goal.GoalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/goals")
@RequiredArgsConstructor
@Tag(name = "Goals", description = "Operations related to goals")
public class GoalController {

    private final GoalService goalService;

    @PostMapping
    @Operation(summary = "Create a goal")
    public ResponseEntity<GoalResponse> create(
            @Valid @RequestBody GoalCreateRequest request) {

        GoalResponse response = goalService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "List all goals")
    public ResponseEntity<List<GoalResponse>> findAll() {
        return ResponseEntity.ok(goalService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a goal by ID")
    public ResponseEntity<GoalResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(goalService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a goal")
    public ResponseEntity<GoalResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody GoalCreateRequest request) {

        return ResponseEntity.ok(goalService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a goal")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        goalService.delete(id);

        return ResponseEntity.noContent().build();
    }
}