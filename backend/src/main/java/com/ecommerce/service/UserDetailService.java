package com.ecommerce.service;

import com.ecommerce.model.UserInfo;
import com.ecommerce.repository.UserDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService {
  private final UserDetailRepository userDetailRepository;

  public UserInfo getUserDetail(String username) {
    return this.userDetailRepository.findById(username).orElse(null);
  }
}
