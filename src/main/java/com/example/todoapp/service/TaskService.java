package com.example.todoapp.service;

import com.example.todoapp.domain.Task;
import com.example.todoapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    //目標の追加
    public Task creatTask(Task task){
        return taskRepository.save(task);
    }

    //idから目標を取得
    public  Optional<Task> getTaskById(Long id){
        return taskRepository.findById(id);
    }

    //全目標を取得
    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }   

    //もし選択したidに目標があった場合に、目標を更新
    public Task updateTask(Long id, Task updatedTask) {
        Optional<Task> existingTask = taskRepository.findById(id);
        if (existingTask.isPresent()) {
            updatedTask.setTaskId(id); 
            return taskRepository.save(updatedTask);
        }
        return null; 
    }
    
    //目標の削除
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

}
