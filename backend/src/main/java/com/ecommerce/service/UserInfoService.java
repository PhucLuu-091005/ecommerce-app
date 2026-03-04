package com.ecommerce.service;

import com.ecommerce.model.UserInfo;
import com.ecommerce.repository.UserDetailRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoService implements UserDetailsService {
  private final UserDetailRepository userDetailRepository;

  @Override
  @NonNull
  public UserInfo loadUserByUsername(@NonNull String username) {
    return this.userDetailRepository.findById(username).orElseThrow(
            () -> new UsernameNotFoundException("No user with username \"" + username + "\"")
    );
  }

  public UserInfo saveUser(UserInfo userInfo) {
    return userDetailRepository.save(userInfo);
  }
}
