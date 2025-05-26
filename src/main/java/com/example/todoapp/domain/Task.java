package com.example.todoapp.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tasks")
@Data

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String title;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "is_completed")
    private boolean completed; 

    @Column(name = "current_streak", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int currentStreak;

    @Column(name = "last_streak_updated")
    private LocalDate lastStreakUpdated;

    @Column(name = "due_date",nullable = true)
    private LocalTime dueDate;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}