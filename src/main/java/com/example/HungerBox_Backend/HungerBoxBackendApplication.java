package com.example.HungerBox_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication


@EnableJpaRepositories
@EnableTransactionManagement
@EntityScan(basePackages = "com/example/HungerBox_Backend/Model")
public class HungerBoxBackendApplication {

	public static void main(String[] args) {
		System.out.println("trial Branch");
		SpringApplication.run(HungerBoxBackendApplication.class, args);
	}
}
