package com.felix.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(requests
      -> requests
        .requestMatchers("/accounts", "/cards", "/loans", "/balances").authenticated()
        .anyRequest().permitAll()
    );

//    http.formLogin(formLoginConfigurer -> {
//      formLoginConfigurer.disable();
//    });

//    http.httpBasic(httpBasicConfigurer -> {
//      httpBasicConfigurer.disable();
//    });

    http.csrf(csrfConfig -> csrfConfig.disable());
    http.formLogin(withDefaults());
    http.httpBasic(withDefaults());

    return http.build();
  }

  /**
   * Provides a delegating password encoder that supports multiple encoding formats (e.g., bcrypt, noop).
   * It automatically selects the correct encoder based on the password prefix.
   *
   * @return a delegating password encoder
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  /**
   * Registers a password checker that verifies whether a given password has been compromised
   * using the HaveIBeenPwned API.
   *
   * This bean should be used during user registration or password updates.
   *
   * @return a compromised password checker
   */
  @Bean
  public CompromisedPasswordChecker compromisedPasswordChecker() {
    return new HaveIBeenPwnedRestApiPasswordChecker();
  }
}


