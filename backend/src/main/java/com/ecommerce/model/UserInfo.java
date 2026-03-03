package com.ecommerce.model;

import jakarta.persistence.*;
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
@NoArgsConstructor
public class UserInfo implements UserDetails {
  @Id
  @Column(length = 100)
  private String userName;

  @Column(nullable = false)
  private String displayName;

  @Column(nullable = false)
  private String hashedPassword;

  private String email;

  private String phoneNumber;

  @Column(nullable = false)
  private String role;

  private String birthday;


  @OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<AddressInfo> addressInfos;

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
