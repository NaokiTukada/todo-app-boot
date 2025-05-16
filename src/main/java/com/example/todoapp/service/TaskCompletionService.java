package com.example.todoapp.service;

import com.example.todoapp.domain.Task;
import com.example.todoapp.domain.TaskCompletion;
import com.example.todoapp.domain.User;
import com.example.todoapp.repository.TaskCompletionRepository;
//後から使用の可能性があるのでコメントアウト
//import com.example.todoapp.repository.TaskRepository;
//import com.example.todoapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class TaskCompletionService {

    @Autowired
    private TaskCompletionRepository taskCompletionRepository;

    //後から使う可能性があるのでコメントアウト
    /*@Autowired
    private TaskRepository taskRepository; 

    @Autowired
    private UserRepository userRepository; */

    //ユーザーの目標が完了したかどうか
    public void completeTask(Task task, User user) {
        TaskCompletion taskCompletion = new TaskCompletion();
        taskCompletion.setTask(task);
        taskCompletion.setUser(user);
        taskCompletion.setCompletedAt(LocalDateTime.now());
        taskCompletionRepository.save(taskCompletion);
    }

    //完了状態の特定の目標を検索（？）し、見つかれば（null出なければ）削除。 
    public void uncompleteTask(Task task, User user) {
        TaskCompletion existingCompletion = taskCompletionRepository.findByTask_TaskIdAndUser_UserId(task.getTaskId(), user.getUserId());
        if (existingCompletion != null) {
            taskCompletionRepository.delete(existingCompletion);
        }
    }

    //このユーザーのこのタスクを今日完了しているか
    public boolean isTaskCompleted(Task task, User user) {
        return taskCompletionRepository.findByTask_TaskIdAndUser_UserId(task.getTaskId(), user.getUserId()) != null;
    }
    
    
    public void resetDailyCompletions(User user) {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);
        List<TaskCompletion> completionsToDelete = taskCompletionRepository.findByUserAndCompletedAtBetween(user, startOfDay, endOfDay);
        taskCompletionRepository.deleteAll(completionsToDelete);
    }

        // 指定された日付の完了記録をリセットする新しいメソッド
    public void resetCompletionsForDate(User user, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        List<TaskCompletion> completionsToDelete = taskCompletionRepository.findByUserAndCompletedAtBetween(user, startOfDay, endOfDay);
        taskCompletionRepository.deleteAllInBatch(completionsToDelete);
    }
}