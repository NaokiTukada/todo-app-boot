package com.example.todoapp.service;

import com.example.todoapp.domain.User;
import com.example.todoapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
public class DailyResetService {

    @Autowired
    private TaskCompletionService taskCompletionService;

    @Autowired
    private UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * *") // 毎日午前0時に実行
    @Transactional
    public void resetCompletionsForAllUsers() {
        List<User> allUsers = userRepository.findAll();
        LocalDate today = LocalDate.now();
        for (User user : allUsers) {
            taskCompletionService.resetCompletionsForDate(user, today);
        }
    }
}