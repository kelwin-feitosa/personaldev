package com.kelwin.personaldev.presentation.controller;

import com.kelwin.personaldev.application.service.ActivityService;
import com.kelwin.personaldev.presentation.dto.activity.ActivityCreateRequest;
import com.kelwin.personaldev.presentation.dto.activity.ActivityResponse;
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
@RequestMapping("/activities")
@RequiredArgsConstructor
@Tag(name = "Activities", description = "Operations related to activities")
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping
    @Operation(summary = "Create an activity")
    public ResponseEntity<ActivityResponse> create(
            @Valid @RequestBody ActivityCreateRequest request) {

        ActivityResponse response = activityService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "List all activities")
    public ResponseEntity<List<ActivityResponse>> findAll() {
        return ResponseEntity.ok(activityService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find an activity by ID")
    public ResponseEntity<ActivityResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(activityService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an activity")
    public ResponseEntity<ActivityResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody ActivityCreateRequest request) {

        return ResponseEntity.ok(activityService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an activity")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        activityService.delete(id);

        return ResponseEntity.noContent().build();
    }
}