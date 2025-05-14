package com.example.todoapp.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data

public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @ManyToOne
    @JoinColumn(name = "user_id",   nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    private LocalDateTime dueDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }



    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
