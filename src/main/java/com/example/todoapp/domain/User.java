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
    
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
}