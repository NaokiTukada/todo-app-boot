package com.example.todoapp.domain;

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