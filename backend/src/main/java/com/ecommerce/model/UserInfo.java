package com.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserInfo {
  @Id
  @Column(name = "LoginName", length = 100)
  private String loginName;

  @Column(name = "UserName", length = 100, nullable = false)
  private String userName;

  @Column(name = "Password", nullable = false)
  private String hashedPassword;

  @Column(name = "Email")
  private String email;

  @Column(name = "PhoneNumber")
  private String phoneNumber;

  @Column(name = "Role", nullable = false)
  private String role;

  @Column(name = "Birthday")
  private String birthday;


//  private Address address;

  @Column(name = "Gender")
  private char gender;
}
