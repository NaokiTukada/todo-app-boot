package com.example.todoapp.controller;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.todoapp.domain.Task;
import com.example.todoapp.domain.User;
import com.example.todoapp.repository.UserRepository;
import com.example.todoapp.service.TaskService;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@RestController
@RequestMapping("/tasks")
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
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> taskOpt = taskService.getTaskById(id);
        return taskOpt.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping
    public List<Task> getAllTasks(@RequestParam Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        userOpt.ifPresent(taskService::whenDateChange);
        return taskService.getAllTasks();
    }
    @PutMapping("/{id}")
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
}