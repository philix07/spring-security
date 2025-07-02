package com.felix.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class MiniBankBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniBankBackendApplication.class, args);
	}

}
