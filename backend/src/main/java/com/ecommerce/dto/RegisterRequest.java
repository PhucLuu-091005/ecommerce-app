package com.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequest {
  private String userName;
  private String displayName;
  private String password;
  private String email;
  private String phoneNumber;
  private String role;
  private String birthday;
  private Character gender;
}
