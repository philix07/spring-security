package com.felix.security.filter;

import com.felix.security.constant.AppConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {

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
    // Extract the JWT token from the request header using a predefined constant (e.g., "Authorization" or custom)
    String jwt = request.getHeader(AppConstants.JWT_HEADER);

    // Check if the token is present
    if (jwt != null) {
      try {
        // Get Spring Environment to retrieve JWT secret from application properties
        Environment env = getEnvironment();

        if (env != null) {
          // Read the secret key from properties; use default if not set
          String secret = env.getProperty(AppConstants.JWT_SECRET_KEY, AppConstants.JWT_SECRET_DEFAULT_VALUE);

          // Create a secret key using the JWT secret (must match signing key used to create JWT)
          SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

          if (secretKey != null) {
            // Parse and validate the JWT using the secret key
            Claims claims = Jwts.parser()
              .verifyWith(secretKey) // attach key for verification
              .build()
              .parseSignedClaims(jwt) // parse and verify the token
              .getPayload(); // extract the payload (body/claims)

            // Extract the "username" from the JWT claims
            String username = String.valueOf(claims.get("username"));

            // Extract authorities as a comma-separated string from JWT
            String authorities = String.valueOf(claims.get("authorities"));

            // Build an Authentication object using extracted username and authorities
            Authentication authentication = new UsernamePasswordAuthenticationToken(
              username, // principal
              null,     // credentials (not needed here)
              AuthorityUtils.commaSeparatedStringToAuthorityList(authorities) // convert to GrantedAuthority list
            );

            // Set the Authentication object into Spring SecurityContext (user is now authenticated)
            SecurityContextHolder.getContext().setAuthentication(authentication);
          }
        }

      } catch (Exception exception) {
        // If anything goes wrong (e.g., invalid token, signature mismatch), throw 401 Unauthorized
        throw new BadCredentialsException("Invalid Token received!");
      }
    }

    // Continue the filter chain (next security filters or controller)
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    // Will not trigger this filter if the end-point (request) path is "/user"
    return request.getServletPath().equals("/user");
  }

}
