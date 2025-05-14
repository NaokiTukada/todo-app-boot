package com.example.todoapp.service;

import com.example.todoapp.domain.Task;
import com.example.todoapp.domain.TaskCompletion;
import com.example.todoapp.domain.User;
import com.example.todoapp.repository.TaskCompletionRepository;
import com.example.todoapp.repository.TaskRepository;
import com.example.todoapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class TaskCompletionService {

    @Autowired
    private TaskCompletionRepository taskCompletionRepository;

    @Autowired
    private TaskRepository taskRepository; // 必要に応じてタスクの存在確認に使う

    @Autowired
    private UserRepository userRepository; // 必要に応じてユーザーの存在確認に使う

    public void completeTask(Task task, User user) {
        TaskCompletion taskCompletion = new TaskCompletion();
        taskCompletion.setTask(task);
        taskCompletion.setUser(user);
        taskCompletion.setCompletedAt(LocalDateTime.now());
        taskCompletionRepository.save(taskCompletion);
    }

    public void uncompleteTask(Task task, User user) {
        TaskCompletion existingCompletion = taskCompletionRepository.findByTask_TaskIdAndUser_UserId(task.getTaskId(), user.getUserId());
        if (existingCompletion != null) {
            taskCompletionRepository.delete(existingCompletion);
        }
    }

    public boolean isTaskCompleted(Task task, User user) {
        return taskCompletionRepository.findByTask_TaskIdAndUser_UserId(task.getTaskId(), user.getUserId()) != null;
    }

  public void resetDailyCompletions(User user) {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);
        List<TaskCompletion> completionsToDelete = taskCompletionRepository.findByUserAndCompletedAtBetween(user, startOfDay, endOfDay);
        taskCompletionRepository.deleteAll(completionsToDelete);
    }
}