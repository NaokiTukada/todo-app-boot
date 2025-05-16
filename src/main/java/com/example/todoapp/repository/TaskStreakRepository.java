    package com.example.todoapp.repository;

    import com.example.todoapp.domain.TaskStreak;
    import com.example.todoapp.domain.TaskStreakId;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;
    import java.util.Optional;

    @Repository
    public interface TaskStreakRepository extends JpaRepository<TaskStreak, TaskStreakId> {
        Optional<TaskStreak> findByUser_UserIdAndTask_TaskId(Long userId, Long taskId);
        Optional<TaskStreak> findByUser_UserId(Long userId);
    }
