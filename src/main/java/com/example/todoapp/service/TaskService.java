package com.example.todoapp.service;

import java.time.LocalDate;
import java.util.List;

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


    //完了状態の時未完了状態に。未完了状態の時完了に(この時、完了時間をつける)！


    //今日初めてログインする時に連続達成日数のカウントするメソッドと今日初めてログインするときに完了状態ならばリセットするメソッドの統合
    public void whenDateChange(User user){
        List<Task> tasks = taskRepository.findByUser(user);
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        for(Task task : tasks) {
           
            if(task.getLastStreakUpdated() != null && task.getLastStreakUpdated().isEqual(today)){
                continue;
            }
            
            if(task.isCompleted()&&
                task.getCompletedAt() != null &&
                task.getCompletedAt().toLocalDate().isEqual(yesterday)){
                
                task.setCurrentStreak(task.getCurrentStreak() + 1);
            
            }else{
                task.setCurrentStreak(0);
            }

            if(task.isCompleted()&&
                task.getCompletedAt() != null &&
                task.getCompletedAt().toLocalDate().isEqual(yesterday)){

                task.setCompleted(false);
            }

            task.setLastStreakUpdated(today);

            taskRepository.save(task);
            
        }
    }

}