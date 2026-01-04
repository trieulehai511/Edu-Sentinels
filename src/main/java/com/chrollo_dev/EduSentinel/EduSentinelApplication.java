package com.chrollo_dev.EduSentinel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EduSentinelApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduSentinelApplication.class, args);
	}

}
