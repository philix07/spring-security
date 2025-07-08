package com.felix.security.config;

import com.felix.security.exception.CustomAccessDeniedHandler;
import com.felix.security.exception.CustomBasicAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Profile("!prod")
@Configuration
public class ProjectSecurityConfig {

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();

    http
      .cors(corsConfig -> corsConfig
        .configurationSource(
          new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
              CorsConfiguration config = new CorsConfiguration();
              // Allow only requests coming from this specific origin (e.g., Angular frontend running locally)
              config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));

              // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
              // In production, it's safer to list only the methods you want to allow
              config.setAllowedMethods(Collections.singletonList("*"));

              // Allow credentials (cookies, authorization headers) to be sent with requests
              config.setAllowCredentials(true);

              // Allow all headers (e.g., Content-Type, Authorization, etc.)
              config.setAllowedHeaders(Collections.singletonList("*"));

              // Cache the CORS response for 3600 seconds (1 hour) to reduce preflight requests
              config.setMaxAge(3600L);
              return config;
            }
          }
        )
      )
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


