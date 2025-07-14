package com.felix.security.filter;

import com.felix.security.constant.AppConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

  /**
   * @param request
   * @param response
   * @param filterChain
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {

    // Retrieve the current authenticated user from the SecurityContext
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // Proceed only if the user is authenticated
    if (authentication != null) {

      // Get Spring Environment to access application properties (like secret key)
      Environment env = getEnvironment();

      if (env != null) {
        // Get the JWT secret key from application properties
        // If not found, fall back to a default secret value
        String secret = env.getProperty(AppConstants.JWT_SECRET_KEY, AppConstants.JWT_SECRET_DEFAULT_VALUE);

        // Convert the secret string into a cryptographic key using HMAC-SHA
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        // Generate a new JWT token
        String jwt = Jwts.builder()
          .issuer("Mini Bank") // Issuer of the token
          .subject("JWT Token") // Subject/title of the token
          .claim("username", authentication.getName()) // Store username in token
          .claim(
            "authorities", // Store authorities (roles/permissions)
            authentication.getAuthorities().stream()
              .map(GrantedAuthority::getAuthority) // Convert to string
              .collect(Collectors.joining(",")) // Join multiple authorities as comma-separated string
          )
          .issuedAt(new Date()) // Token issue time
          .expiration(new Date((new Date()).getTime() + 30000000)) // Token expiry time (e.g., ~8 hours later)
          .signWith(secretKey) // Sign the token with the secret key
          .compact(); // Create the compact JWT string

        // Set the generated JWT token in the response header
        response.setHeader(AppConstants.JWT_HEADER, jwt);
      }
    }

    // Continue the filter chain (let request go through)
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    // Will not trigger this filter if the end-point (request) path is not "/user"
    return !request.getServletPath().equals("/user");
  }
}
