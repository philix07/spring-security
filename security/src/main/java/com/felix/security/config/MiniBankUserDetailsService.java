package com.felix.security.config;

import com.felix.security.entity.AppUser;
import com.felix.security.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MiniBankUserDetailsService implements UserDetailsService {

  private final AppUserRepository appUserRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AppUser appUser = appUserRepository
      .findByEmail(username)
      .orElseThrow(() -> new UsernameNotFoundException("There's no account with username: " + username));

    List<GrantedAuthority> grantedAuthorities = appUser.getAuthorities().stream()
      .map(authority -> new SimpleGrantedAuthority(authority.getName()))
      .collect(Collectors.toList());

    return new User(appUser.getEmail(), appUser.getPwd(), grantedAuthorities);
  }

}
