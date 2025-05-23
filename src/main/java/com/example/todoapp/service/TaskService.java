package com.example.todoapp.service;

import com.example.todoapp.domain.Task;
import com.example.todoapp.domain.User;
import com.example.todoapp.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;  
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    //目標の追加INSERTとかUPDATEとか
    public Task createTask(Task task){
        return taskRepository.save(task);
    }

    //idから目標を取得SELECT tasks WHERE id てきな？
    public Optional<Task> getTaskById(Long id){
        return taskRepository.findById(id);
    }
    
    //多分tasksの全項目を取得するてきな　SELECT tasksで出てきた項目をすべて合わせて取得的な？
    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }
    
    //正確なUPDATE文！もしidがあるならidのアップデートと新しいタスクの追加（上書き）
    public Optional<Task> updateTask(Long id, Task updateTask) {
        Optional<Task> existenceTask = taskRepository.findById(id);
        if (existenceTask.isPresent()) {
            updateTask.setTaskId(id);
            return Optional.of(taskRepository.save(updateTask));
        }
        return Optional.empty();
    }

    //目標の削除
    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }

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