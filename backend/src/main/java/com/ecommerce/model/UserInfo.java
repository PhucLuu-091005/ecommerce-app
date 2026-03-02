package com.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo implements UserDetails {
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

  @Override
  @NonNull
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role));
  }

  @Override
  @NonNull
  public String getPassword() {
    return hashedPassword;
  }

  @Override
  @NonNull
  public String getUsername() {
    return userName;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
