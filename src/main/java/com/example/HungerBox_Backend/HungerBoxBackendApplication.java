package com.example.HungerBox_Backend;

import com.example.HungerBox_Backend.Service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication

@EnableScheduling
@EnableJpaRepositories(basePackages = "com.example.HungerBox_Backend.Repository")
@EnableTransactionManagement
@EntityScan(basePackages = "com/example/HungerBox_Backend/Model")
public class HungerBoxBackendApplication {

	@Autowired
	private EmailSenderService senderService;

	public static void main(String[] args) {
		System.out.println("trial Branch");
		SpringApplication.run(HungerBoxBackendApplication.class, args);
	}
}
