package com.example.todoapp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todoapp.domain.Task;
import com.example.todoapp.domain.User;
import com.example.todoapp.repository.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    //目標の追加INSERT


    //idから目標を取得
 

    //tasksの全項目を取得する

    
    //UPDATE文(編集)


    //目標の削除


    //あるユーザーが持ってるすべてのタスクを取得
    public List<Task> findAllByUser(User user){
        return taskRepository.findByUser(user);
    }

    //完了状態の時未完了状態に。未完了状態の時完了に(この時、完了時間をつける)！
    public void toggleTaskCompletion(Long taskId) {
    Optional<Task> optionalTask = taskRepository.findById(taskId);
    
        if (optionalTask.isEmpty()) return;
        Task task = optionalTask.get();

        if (task.isCompleted()) {
            task.setCompleted(false);

        } else {
            task.setCompleted(true);
            task.setCompletedAt(LocalDateTime.now());
        }

    taskRepository.save(task);
    }
    

    //今日初めてログインする時に連続達成日数のカウントするメソッドと今日初めてログインするときに完了状態ならばリセットするメソッドの統合

}