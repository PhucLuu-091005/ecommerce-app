package com.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequest {
  // Common fields
  private String userName;
  private String displayName;
  private String password;
  private String email;
  private String phoneNumber;
  private String role; // BUYER | SELLER
  private String birthday;
  private Character gender;

  // Seller-only fields (required when role = SELLER)
  private String shopName;
  private String citizenIDCard;
  private String sellerName;
}
