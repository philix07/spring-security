package com.felix.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 👇 This filter ensures that the CSRF token is generated and stored in a cookie
// so that it can be accessed by the frontend (e.g., JavaScript framework like Angular or React).
public class CsrfCookieFilter extends OncePerRequestFilter {

  /**
   * This method is called once per request by Spring Security.
   * It ensures the CSRF token is initialized and sent to the client (typically in a cookie).
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {

    // 👇 Retrieves the CSRF token from the request attributes.
    // Spring stores it here when CSRF protection is enabled.
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

    // 👇 Ensures the token is resolved.
    // Calling getToken() causes Spring's lazy/deferred token logic to load the actual token value,
    // triggering the CookieCsrfTokenRepository to write it as a cookie.
    csrfToken.getToken();

    // 👇 Continue processing the rest of the filter chain (pass the request forward)
    filterChain.doFilter(request, response);
  }
}
