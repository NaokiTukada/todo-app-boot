package com.example.todoapp.repository;

import com.example.todoapp.domain.TaskCompletion;
import com.example.todoapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskCompletionRepository extends JpaRepository<TaskCompletion, Long> {
    //特定の目標とユーザーの完了履歴を1件取得
    TaskCompletion findByTask_TaskIdAndUser_UserId(Long taskId, Long userId);

     // 特定のユーザーの全ての完了履歴を検索するメソッド
    List<TaskCompletion> findByUser(User user);

    // 特定のユーザーの指定期間の完了履歴を検索するメソッド（例：今日）
    List<TaskCompletion> findByUserAndCompletedAtBetween(User user, LocalDateTime startTime, LocalDateTime endTime);
}