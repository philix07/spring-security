package com.felix.security.config;

import com.felix.security.entity.AppUser;
import com.felix.security.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@RequiredArgsConstructor
@Profile("prod")
@Component
public class MinibankUsernameProdPwdAuthenticationProvider implements AuthenticationProvider {

  private final AppUserRepository appUserRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String rawPassword = authentication.getCredentials().toString();

    AppUser user = appUserRepository.findByEmail(username)
      .orElseThrow(() -> new BadCredentialsException("User not found"));

    if (!passwordEncoder.matches(rawPassword, user.getPwd())) {
      throw new BadCredentialsException("Invalid password");
    }

    // You can return multiple authorities if needed
    return new UsernamePasswordAuthenticationToken(
      user.getEmail(),
      user.getPwd(),
      Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString()))
    );
  }

  @Override
  public boolean supports(Class<?> authentication) {
    // This makes sure this provider only handles UsernamePasswordAuthenticationToken
    return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
  }
}
