package com.example.todoapp.repository;

//Userオブジェクトをデータベースに保存や検索などをするための窓口。

import com.example.todoapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}   