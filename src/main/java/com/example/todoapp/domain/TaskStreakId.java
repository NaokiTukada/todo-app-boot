package com.example.todoapp.domain;

import java.io.Serializable;
import lombok.*;

@Data
public class TaskStreakId implements Serializable{
    private Long task;
    private Long user;

    public TaskStreakId(Long task, Long user) {
        this.task = task;
        this.user = user;
    }    
}
