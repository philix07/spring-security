package com.felix.security.config;

import com.felix.security.exception.CustomAccessDeniedHandler;
import com.felix.security.exception.CustomBasicAuthenticationEntryPoint;
import com.felix.security.filter.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Profile("!prod")
@Configuration
public class ProjectSecurityConfig {

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();

    http
      .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
      .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
      .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
      .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
      .addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
      .authorizeHttpRequests(requests -> requests
        .requestMatchers("/myAccount").hasRole("USER")
        .requestMatchers("/myBalance").hasAnyRole("USER", "ADMIN")
        .requestMatchers("/myLoans").authenticated()
        .requestMatchers("/myCards").hasRole("USER")
        .requestMatchers("/user").authenticated()
        .requestMatchers("/notices", "/contact", "/error", "/register", "/invalidSession", "/apiLogin").permitAll()
      )
      .csrf(csrfConfig -> csrfConfig
        // 👇 Specifies a custom handler for resolving CSRF tokens from the request.
        // Useful for advanced scenarios like SPAs that need to access the token in a non-standard way.
        .csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)

        // 👇 Disables CSRF protection for specific endpoints (e.g., public forms).
        // These paths can be safely excluded if they don't modify state or if other protection is in place.
        .ignoringRequestMatchers("/contact", "/register", "/apiLogin")

        // 👇 Stores the CSRF token in a cookie so it can be accessed by frontend JavaScript (HttpOnly=false).
        // This is commonly used in single-page applications (e.g., Angular, React) to read the token
        // and include it in custom headers (like `X-XSRF-TOKEN`) on each request.
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
      )
      .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)

      .cors(corsConfig -> corsConfig
        .configurationSource(new CorsConfigurationSource() {
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

            config.setExposedHeaders(Arrays.asList("Authorization"));

            // Cache the CORS response for 3600 seconds (1 hour) to reduce preflight requests
            config.setMaxAge(3600L);
            return config;
          }
        })
      )
      .sessionManagement(smc -> smc
        // tells Spring to always create a session if it doesn't exist yet,
        // even for unauthenticated users.
        .sessionCreationPolicy(SessionCreationPolicy.NEVER)
        // in real project we're suppose to build an actual HTML pages to show more
        // details to the end user when an session has ended.
        .invalidSessionUrl("/invalidSession")
        // only 3 concurrent session can created at a time
        .maximumSessions(3)
      )
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

  @Bean
  public AuthenticationManager authenticationManager(
    UserDetailsService userDetailsService,
    PasswordEncoder passwordEncoder
  ) {
    MinibankUsernamePwdAuthenticationProvider authenticationProvider = new MinibankUsernamePwdAuthenticationProvider(
      userDetailsService, passwordEncoder
    );

    ProviderManager providerManager = new ProviderManager(authenticationProvider);
    providerManager.setEraseCredentialsAfterAuthentication(false);

    return providerManager;
  }
}


