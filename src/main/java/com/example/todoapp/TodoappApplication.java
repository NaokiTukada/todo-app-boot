package com.example.todoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@SpringBootApplication
public class TodoappApplication {
	@RequestMapping("/register")
	public String register() {
		return "register";
	}

	public static void main(String[] args) {
		SpringApplication.run(TodoappApplication.class, args);
	}
}