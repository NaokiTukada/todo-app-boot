package com.example.todoapp.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "task_streaks") 
@Data
@IdClass(TaskStreakId.class) // 複合主キーを使うことを示す
public class TaskStreak implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false, referencedColumnName = "taskId")
    private Task task;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "userId")
    private User user;

    @Column(name = "current_streak", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int currentStreak;

    @Column(name = "last_completion_date")
    private LocalDate lastCompletionDate;


}