package com.example.todoapp.service;

import com.example.todoapp.domain.Task;
import com.example.todoapp.domain.TaskCompletion;
import com.example.todoapp.domain.TaskStreak;
import com.example.todoapp.domain.TaskStreakId;
import com.example.todoapp.domain.User;
import com.example.todoapp.repository.TaskCompletionRepository;
import com.example.todoapp.repository.TaskRepository;
import com.example.todoapp.repository.TaskStreakRepository;
import com.example.todoapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskStreakService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStreakRepository taskStreakRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskCompletionRepository taskCompletionRepository;

    //このユーザーが目標に対して何日連続で達成したか
    public TaskStreak getOrCreateTaskStreak(User user, Task task) {
        TaskStreakId id = new TaskStreakId(task.getTaskId(), user.getUserId());
        Optional<TaskStreak> taskStreakOptional = taskStreakRepository.findById(id);
        return taskStreakOptional.orElseGet(() -> {
            TaskStreak newTaskStreak = new TaskStreak();
            newTaskStreak.setTask(task);
            newTaskStreak.setUser(user);
            newTaskStreak.setCurrentStreak(0);
            newTaskStreak.setLastCompletionDate(null);
            newTaskStreak.setAllTasksCurrentStreak(0);
            newTaskStreak.setLastAllTasksCompletionDate(null);
            return taskStreakRepository.save(newTaskStreak);
        });
    }

    //その日の目標が完了してかつ昨日も完了なら＋１、そうでなければ０から
    public void incrementTaskStreak(User user, Task task) {
        TaskStreak taskStreak = getOrCreateTaskStreak(user, task);
        LocalDate today = LocalDate.now();

        if (taskStreak.getLastCompletionDate() != null
                && taskStreak.getLastCompletionDate().plusDays(1).isEqual(today)) {
            // 昨日もやってて今日も完了 → streak +1
            taskStreak.setCurrentStreak(taskStreak.getCurrentStreak() + 1);
        } else if (taskStreak.getLastCompletionDate() == null
                || !taskStreak.getLastCompletionDate().isEqual(today)) {
            // 初回、または連続していない → 0からスタート +1 = 1
            taskStreak.setCurrentStreak(1);
        }

        // 最後の完了日を今日に更新
        taskStreak.setLastCompletionDate(today);

        // 保存
        taskStreakRepository.save(taskStreak);
    }
    
    //指定されたstreakを０にリセット。完了日もnullに
    public void resetTaskStreak(User user, Task task) {
        Optional<TaskStreak> taskStreakOptional = taskStreakRepository.findByUser_UserIdAndTask_TaskId(user.getUserId(), task.getTaskId());
        taskStreakOptional.ifPresent(taskStreak -> {
            taskStreak.setCurrentStreak(0);
            taskStreak.setLastCompletionDate(null);
            taskStreakRepository.save(taskStreak);
        });
    }

    //毎日０時にログインしたか確認→してなければ連続日数をキャンセル。
    public void processDailyStreak(User user) {
        Optional<User> userOptional = userRepository.findById(user.getUserId());
        if (userOptional.isPresent()) {
            User currentUser = userOptional.get();
            LocalDate today = LocalDate.now();
            if (currentUser.getLastLoginDate() == null || !currentUser.getLastLoginDate().isEqual(today)) {
                List<TaskStreak> taskStreaks = taskStreakRepository.findByUser_UserId(currentUser.getUserId()).orElse(List.of());
                for (TaskStreak taskStreak : taskStreaks) {
                    taskStreak.setCurrentStreak(0);
                    taskStreak.setLastCompletionDate(null);
                    taskStreakRepository.save(taskStreak);
                }
            }
            currentUser.setLastLoginDate(today);
            userRepository.save(currentUser);
            updateAllTasksStreak(currentUser);
        }
    }

    //あるユーザーが持っている一番短い目標連続達成を取得    
    public int calculateMinTaskStreakForUser(User user) {
    List<TaskStreak> userTaskStreaks = taskStreakRepository.findByUser_UserId(user.getUserId()).orElse(List.of());

    if (userTaskStreaks.isEmpty()) {
        return 0; // タスク streak がない場合は 0 を返す
    }

    return userTaskStreaks.stream()
            .mapToInt(TaskStreak::getCurrentStreak)
            .min()
            .orElse(0);
    }

    //あるユーザーが、ある目標に対してどれだけ連続でやっているかを探す
    public Optional<TaskStreak> getTaskStreak(User user, Task task) {
        return taskStreakRepository.findByUser_UserIdAndTask_TaskId(user.getUserId(), task.getTaskId());
    }


}