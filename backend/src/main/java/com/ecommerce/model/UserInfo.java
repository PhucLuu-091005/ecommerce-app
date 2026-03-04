package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "UserInfo")
public class UserInfo implements UserDetails {
  @Id
  @Column(length = 100)
  private String userName;

  @Column(nullable = false)
  private String displayName;

  @Column(nullable = false)
  private String hashedPassword;

  private String email;

  @Column(columnDefinition = "CHAR(10)")
  private String phoneNumber;

  @Column(columnDefinition = "CHAR(1)")
  private Character gender;

  private LocalDate birthDate;

  private String address;

  @Column(nullable = false, length = 20)
  private String role = "BUYER"; // BUYER | SELLER | ADMIN

  @OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<AddressInfo> addressInfos;

  @Override
  @NonNull
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + role));
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
}
