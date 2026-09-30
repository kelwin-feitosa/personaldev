package com.kelwin.personaldev.presentation.controller;

import com.kelwin.personaldev.application.service.ActivityExecutionService;
import com.kelwin.personaldev.presentation.dto.activityexecution.ActivityExecutionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/executions")
@RequiredArgsConstructor
@Tag(name = "Activity Executions", description = "Endpoints for managing activity executions")
public class ActivityExecutionController {

    private final ActivityExecutionService service;

    @PostMapping("/activities/{activityId}")
    @Operation(
            summary = "Start an activity execution",
            description = "Starts a new execution for an in-progress activity"
    )
    public ResponseEntity<ActivityExecutionResponse> start(
            @PathVariable UUID activityId
    ) {
        return ResponseEntity.ok(service.start(activityId));
    }

    @PostMapping("/{executionId}/finish")
    @Operation(
            summary = "Finish an activity execution",
            description = "Finishes an active execution and calculates its actual duration"
    )
    public ResponseEntity<ActivityExecutionResponse> finish(
            @PathVariable UUID executionId
    ) {
        return ResponseEntity.ok(service.finish(executionId));
    }

    @GetMapping
    @Operation(
            summary = "List activity executions",
            description = "Returns all activity executions"
    )
    public ResponseEntity<List<ActivityExecutionResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Find activity execution by ID",
            description = "Returns an activity execution by its ID"
    )
    public ResponseEntity<ActivityExecutionResponse> findById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete activity execution",
            description = "Deletes an activity execution by its ID"
    )
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}