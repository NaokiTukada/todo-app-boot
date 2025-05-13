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

    public Task creatTask(Task task){
        return taskRepository.save(task);
    }

    public  Optional<Task> getTaskById(Long id){
        return taskRepository.findById(id);
    }

    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    public Task updateTask(Long id, Task updatedTask) {
        Optional<Task> existingTask = taskRepository.findById(id);
        if (existingTask.isPresent()) {
            updatedTask.setTaskId(id); // IDを更新対象のタスクに設定
            return taskRepository.save(updatedTask);
        }
        return null; // または例外をスローするなど、エラーハンドリングを行う
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

}
