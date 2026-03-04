package com.ecommerce.dto;

import lombok.*;

/**
 * DTO for login and register response containing JWT token, username, and user role.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
  private String token;
  private String username;
  private String role;
}
