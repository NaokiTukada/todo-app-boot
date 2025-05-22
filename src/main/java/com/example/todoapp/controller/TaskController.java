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
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Optional<User> userOpt = userRepository.findById(task.getUser().getUserId());
        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();

        task.setUser(userOpt.get());
        Task savedTask = taskService.createTask(task);
        return ResponseEntity.ok(savedTask);
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }


    @GetMapping("/{id}")


    @PutMapping("/{id}")
 

    @DeleteMapping("/{id}")


    
    @PutMapping("/{id}/toggle")



    @PostMapping("/reset-daily/{userId}")

}














