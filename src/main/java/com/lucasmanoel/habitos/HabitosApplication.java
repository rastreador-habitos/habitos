package com.lucasmanoel.habitos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HabitosApplication {

	public static void main(String[] args) {
		SpringApplication.run(HabitosApplication.class, args);
	}

}