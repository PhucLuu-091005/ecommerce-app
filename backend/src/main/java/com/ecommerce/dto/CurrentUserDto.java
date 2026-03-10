package com.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CurrentUserDto {
  private String username;
  private String email;
  private String phoneNumber;
  private String role;  // "BUYER", "SELLER", "ADMIN"
  
  // Seller-specific fields (null if not seller)
  private String shopName;
  private Long earnings;
  
  // Buyer-specific fields (null if not buyer)
  private Long spending;
}
