package com.felix.security.config;

import com.felix.security.exception.CustomAccessDeniedHandler;
import com.felix.security.exception.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Profile("!prod")
@Configuration
public class ProjectSecurityConfig {

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http
      .sessionManagement(smc -> smc
        // in real project we're suppose to build an actual HTML pages to show more
        // details to the end user when an session has ended.
        .invalidSessionUrl("/invalidSession")
        // only 3 concurrent session can created at a time
        .maximumSessions(3)
      )
      .authorizeHttpRequests(requests -> requests
        .requestMatchers("/accounts", "/cards", "/loans", "/balances").authenticated()
        .requestMatchers("/notices", "/contacts", "/error", "/register", "/invalidSession").permitAll()
      )

      .csrf(csrfConfig -> csrfConfig.disable())
      .formLogin(withDefaults())
      .httpBasic(hbc -> hbc
        .authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint())
      )
      .exceptionHandling(ehc -> ehc
        .accessDeniedHandler(new CustomAccessDeniedHandler())
      );

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
    // this is the default config
    //return PasswordEncoderFactories.createDelegatingPasswordEncoder();

    // specify BCrypt as the Password Encoder
    return new BCryptPasswordEncoder();
  }

}


