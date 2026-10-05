package com.kelwin.personaldev.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.kelwin.personaldev.domain.model.enums.ReviewPerformance;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "knowledge")
@Getter 
@Setter 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
public class Knowledge {

    @Id 
    @GeneratedValue 
    private UUID id;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(
        mappedBy = "knowledge",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @Builder.Default
    private List<KnowledgeReview> reviews = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime nextReviewAt;

    @Column(nullable = false)
    private Integer reviewIntervalDays;
    

    public void update(
        String title,
        String description
    ) {
        this.title = title;
        this.description = description;
    }

    public void registerReview(
            ReviewPerformance performance,
            LocalDateTime reviewedAt
    ) {
        
    }
}