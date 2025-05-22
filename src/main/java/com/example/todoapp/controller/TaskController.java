package com.example.todoapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todoapp.domain.Task;
import com.example.todoapp.domain.User;
import com.example.todoapp.repository.UserRepository;
import com.example.todoapp.service.TaskService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController 
@RequestMapping("api/tasks")
public class TaskController {

    
    private final TaskService taskService;

   
    private final UserRepository userRepository;

    @PostMapping


    @GetMapping

    @GetMapping("/{id}")


    @PutMapping("/{id}")
 

    @DeleteMapping("/{id}")


    
    @PutMapping("/{id}/toggle")
    public ResponseEntity<Void> toggleTaskCompletion(@PathVariable Long id) {
        taskService.toggleTaskCompletion(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-daily/{userId}")
    public ResponseEntity<Void> resetDailyTasks(@PathVariable Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        taskService.whenDateChange(userOpt.get());
        return ResponseEntity.ok().build();
    }
    
}













