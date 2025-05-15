package com.example.todoapp.domain;

//「todoapp_db」データベースの「User」テーブルの構造。

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "User") // テーブル名はUser
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    
    @Column(length = 256, nullable = false, unique = true)
    private String email;

    @Column(length = 256, nullable = false)
    private String password;
}