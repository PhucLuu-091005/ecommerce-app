package com.ecommerce.dto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginResponse {
  private final String token;
  private final String username;
  private final String role;
}
