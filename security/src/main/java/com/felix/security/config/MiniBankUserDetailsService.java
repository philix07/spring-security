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

@Service
@RequiredArgsConstructor
public class MiniBankUserDetailsService implements UserDetailsService {

  private final AppUserRepository appUserRepository;

  /**
   * @param username the username identifying the user's email whose data is required.
   * @return UserDetails
   * @throws UsernameNotFoundException
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    AppUser appUser = appUserRepository
      .findByEmail(username)
      .orElseThrow(() -> new UsernameNotFoundException("There's no account with username: " + username));

    List<GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority(appUser.getRole().toString()));
    return new User(appUser.getEmail(), appUser.getPwd(), grantedAuthorities);
  }

}
