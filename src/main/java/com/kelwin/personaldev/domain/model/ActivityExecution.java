package com.kelwin.personaldev.domain.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import com.kelwin.personaldev.presentation.exception.DomainRuleException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "activity_executions")
@Getter 
@Setter 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
public class ActivityExecution {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private Integer actualDuration;

    public void finish() {
        if (endedAt != null) {
            throw new DomainRuleException(
                    "Activity execution is already finished"
            );
        }

        endedAt = LocalDateTime.now();
        actualDuration = (int) Duration.between(startedAt, endedAt).toMinutes();
    }
}