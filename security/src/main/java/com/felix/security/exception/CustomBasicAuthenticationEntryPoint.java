package com.felix.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
    throws IOException, ServletException {
    // Populate dynamic values
    String message =
      (authException != null && authException.getMessage() != null) ? authException.getMessage() : "Unauthorized";
    LocalDateTime currentTimeStamp = LocalDateTime.now();
    String path = request.getRequestURI();

    response.setHeader("minibank-error-reason", "Authentication failed");
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json;charset=UTF-8");


    // construct json response with it's format
    String jsonResponse =
      String.format(
        "{\"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"message\": \"%s\", \"path\": \"%s\"}",
        currentTimeStamp,
        HttpStatus.UNAUTHORIZED.value(),
        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
        message, path
      );
    response.getWriter().write(jsonResponse);
  }
}
