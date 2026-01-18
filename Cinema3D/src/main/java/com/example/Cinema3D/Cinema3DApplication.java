package com.example.Cinema3D;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Cinema3DApplication {

	public static void main(String[] args) {
		SpringApplication.run(Cinema3DApplication.class, args);
	}

}
