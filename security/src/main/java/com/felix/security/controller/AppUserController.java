package com.felix.security.controller;

import com.felix.security.constant.AppConstants;
import com.felix.security.dto.LoginRequestDTO;
import com.felix.security.dto.LoginResponseDTO;
import com.felix.security.entity.AppUser;
import com.felix.security.repository.AppUserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AppUserController {

  private final AppUserRepository customerRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final Environment env;

  @PostMapping("/register")
  public ResponseEntity<String> registerUser(@RequestBody AppUser user) {
    try {
      String hashPwd = passwordEncoder.encode(user.getPwd());
      user.setPwd(hashPwd);
      user.setCreateDt(new Date(System.currentTimeMillis()));
      AppUser savedUser = customerRepository.save(user);

      if (savedUser.getId() > 0) {
        return ResponseEntity.status(HttpStatus.CREATED).
          body("Given user details are successfully registered");
      } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
          body("User registration failed");
      }
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
        body("An exception occurred: " + ex.getMessage());
    }
  }

  @RequestMapping("/user")
  public AppUser getUserDetailsAfterLogin(Authentication authentication) {
    Optional<AppUser> optionalCustomer = customerRepository.findByEmail(authentication.getName());
    return optionalCustomer.orElse(null);
  }

  @PostMapping("/apiLogin")
  public ResponseEntity<LoginResponseDTO> apiLogin(@RequestBody LoginRequestDTO loginRequest) {
    String jwt = "";
    Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(
      loginRequest.username(), loginRequest.password()
    );

    Authentication authenticationResponse = authenticationManager.authenticate(authentication);
    if (authenticationResponse != null && authenticationResponse.isAuthenticated()) {
      if (env != null) {
        String secret = env.getProperty(AppConstants.JWT_SECRET_KEY, AppConstants.JWT_SECRET_DEFAULT_VALUE);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        jwt = Jwts.builder()
          .issuer("Mini Bank") // Issuer of the token
          .subject("JWT Token") // Subject/title of the token
          .claim("username", authenticationResponse.getName()) // Store username in token
          .claim(
            "authorities", // Store authorities (roles/permissions)
            authenticationResponse.getAuthorities().stream()
              .map(GrantedAuthority::getAuthority) // Convert to string
              .collect(Collectors.joining(",")) // Join multiple authorities as comma-separated string
          )
          .issuedAt(new java.util.Date()) // Token issue time
          .expiration(new java.util.Date((new java.util.Date()).getTime() + 30000000)) // Token expiry time (e.g., ~8 hours later)
          .signWith(secretKey) // Sign the token with the secret key
          .compact(); // Create the compact JWT string
      }
    }

    return ResponseEntity
      .status(HttpStatus.OK)
      .header(AppConstants.JWT_HEADER, jwt)
      .body(new LoginResponseDTO(HttpStatus.OK.getReasonPhrase(), jwt));
  }

}
