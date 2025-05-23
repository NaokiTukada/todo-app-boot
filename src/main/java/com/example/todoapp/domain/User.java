package com.example.todoapp.domain;

import java.time.LocalDateTime;

//「todoapp_db」データベースの「users」テーブルの構造。

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "users") // テーブル名はusers
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    
    @Column(length = 256, nullable = false, unique = true)
    private String email;

    @Column(length = 256, nullable = false)
    private String password;

    private LocalDateTime lastLogin;//最終ログイン日時
}