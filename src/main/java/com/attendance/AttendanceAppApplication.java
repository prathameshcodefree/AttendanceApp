package com.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication

public class AttendanceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttendanceAppApplication.class, args);
	}

}
