package com.example.todoapp.controller;

import com.example.todoapp.domain.Task;
import com.example.todoapp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

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
}
