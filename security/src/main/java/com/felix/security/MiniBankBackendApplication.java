package com.felix.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableWebSecurity(debug = true)
public class MiniBankBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(MiniBankBackendApplication.class, args);
  }

}
