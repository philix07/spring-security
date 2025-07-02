package com.felix.security.controller;

import com.felix.security.entity.AppUser;
import com.felix.security.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AppUserController {

  private final AppUserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @PostMapping("/register")
  public ResponseEntity<String> registerUser(@RequestBody AppUser appUser) {
    try {
      String hashedPassword = passwordEncoder.encode(appUser.getPassword());
      appUser.setPassword(hashedPassword);

      AppUser savedUser = userRepository.save(appUser);

      if(savedUser.getId() > 0) {
        return ResponseEntity.status(HttpStatus.CREATED)
          .body("User successfully created");
      } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("User registration failed");
      }
    } catch (Exception e) {
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("An exception occured: " + e.getMessage());
    }
  }

}
