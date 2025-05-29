package com.example.todoapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.todoapp.domain.Task;
import com.example.todoapp.domain.User;
import com.example.todoapp.repository.UserRepository;
import com.example.todoapp.service.TaskService;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email).orElseThrow();
        task.setUser(user);
        Task savedTask = taskService.createTask(task);
        return ResponseEntity.ok(savedTask);
    }

    @GetMapping
    public String listTasks(Model model, Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email).orElseThrow();

        List<Task> tasks = taskService.getTasksByUser(user);
        List<Long> completedTaskIds = tasks.stream()
                                           .filter(Task::isCompleted)
                                           .map(Task::getTaskId)
                                           .toList();

        int minStreak = tasks.stream()
                .mapToInt(Task::getCurrentStreak)
                .min()
                .orElse(0);

        model.addAttribute("tasks", tasks);
        model.addAttribute("completedTaskIds", completedTaskIds);
        model.addAttribute("userEmail", email);
        model.addAttribute("allTasksStreakCount", minStreak);
        model.addAttribute("allTasksCompleted", false);  
        model.addAttribute("allTasksStreakCount", 0);    
        model.addAttribute("showResetButton", false);    

        return "task_list";
    }


    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> taskOpt = taskService.getTaskById(id);
        return taskOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        return taskService.updateTask(id, updatedTask)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}/toggle")
    public ResponseEntity<Void> toggleTaskCompletion(@PathVariable Long id) {
        taskService.toggleTaskCompletion(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/{userId}/updateStreak")
    public ResponseEntity<Void> updateUserTasks(@PathVariable Long userId) {
        taskService.whenDateChange(userId);
        return ResponseEntity.ok().build();
    }


}