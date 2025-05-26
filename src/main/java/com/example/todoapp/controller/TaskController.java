package com.example.todoapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Optional<User> userOpt = userRepository.findById(task.getUser().getUserId());
        if (userOpt.isEmpty())
            return ResponseEntity.notFound().build();
        task.setUser(userOpt.get());
        Task savedTask = taskService.createTask(task);
        return ResponseEntity.ok(savedTask);
    }

    @GetMapping
    public String ListTasks(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);

        // 必要な変数をダミー値で追加（本来はサービス等から取得）
        model.addAttribute("userEmail", "dummy@example.com");
        model.addAttribute("completedTaskIds", List.of());
        model.addAttribute("allTasksCompleted", false);
        model.addAttribute("allTasksStreakCount", 0);
        model.addAttribute("showResetButton", false);

        return "task_list";
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        Optional<Task> taskOpt = taskService.getTaskById(taskId);
        return taskOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task updatedTask) {
        return taskService.updateTask(taskId, updatedTask)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{taskId}/toggle")
    public ResponseEntity<Void> toggleTaskCompletion(@PathVariable Long taskId) {
        taskService.toggleTaskCompletion(taskId);
        return ResponseEntity.ok().build();
    }

}