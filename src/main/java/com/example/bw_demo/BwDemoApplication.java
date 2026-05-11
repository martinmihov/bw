package com.example.bw_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class BwDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BwDemoApplication.class, args);
	}
}
