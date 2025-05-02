package com.example.todoapp; // 自分のパッケージに合わせて

import jakarta.persistence.*;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private boolean done;

    public Task() {
    }

    public Task(String title) {
        this.title = title;
        this.done = false;
    }

    // getter / setter は省略せず作りましょう！
}

