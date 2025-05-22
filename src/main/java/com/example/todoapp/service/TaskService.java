package com.example.todoapp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todoapp.domain.Task;
import com.example.todoapp.repository.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    //目標の追加INSERT


    //idから目標を取得
 

    //tasksの全項目を取得する
    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }
    
    //UPDATE文(編集)
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


    //完了状態の時未完了状態に。未完了状態の時完了に(この時、完了時間をつける)！


    //今日初めてログインする時に連続達成日数のカウントするメソッドと今日初めてログインするときに完了状態ならばリセットするメソッドの統合

}
