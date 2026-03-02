package com.ecommerce.dto;

import lombok.Data;

@Data
public class UserDetailDto {
  private String loginName;
  private String userName;
  private String email;
  private String phoneNumber;
  private String role;
  private String birthday;
  private Character gender;
}
