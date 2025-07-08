package com.felix.security.controller;

import com.felix.security.entity.AppUser;
import com.felix.security.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AppUserController {

  private final AppUserRepository customerRepository;
  private final PasswordEncoder passwordEncoder;

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

}
