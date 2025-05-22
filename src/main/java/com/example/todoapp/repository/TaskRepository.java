package com.example.todoapp.repository;


import com.example.todoapp.domain.Task;
import com.example.todoapp.domain.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long>{
    List<Task> findByUser(User user);
}   