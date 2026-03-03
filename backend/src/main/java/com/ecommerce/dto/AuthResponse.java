package com.ecommerce.dto;

import lombok.RequiredArgsConstructor;

/**
 * DTO for login and register response containing JWT token, username, and user role.
 */
@RequiredArgsConstructor
public class AuthResponse {
  private final String token;
  private final String username;
  private final String role;
}
