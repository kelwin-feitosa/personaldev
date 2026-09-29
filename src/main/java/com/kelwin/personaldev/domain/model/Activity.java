package com.kelwin.personaldev.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import com.kelwin.personaldev.domain.model.enums.ActivityStatus;

@Entity
@Table(name = "activities")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) //Carregar só quando necessário
    @JoinColumn(name = "goal_id")
    private Goal goal;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    private Integer estimatedDuration;

    private Integer difficulty;

    private Integer priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        status = ActivityStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void update(
            String title,
            String description,
            Integer estimatedDuration,
            Integer difficulty,
            Integer priority,
            Goal goal
    ) {
        this.title = title;
        this.description = description;
        this.estimatedDuration = estimatedDuration;
        this.difficulty = difficulty;
        this.priority = priority;
        this.goal = goal;
    }
}