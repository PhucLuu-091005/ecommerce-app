package com.ecommerce.service;

import com.ecommerce.dto.LoginRequest;
import com.ecommerce.dto.AuthResponse;
import com.ecommerce.dto.RegisterRequest;
import com.ecommerce.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserInfoService userInfoService;
  private final PasswordEncoder passwordEncoder;

  public AuthResponse login(LoginRequest loginRequest) {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
    );
    String token = jwtService.generateJwtToken(loginRequest.getUsername());
    UserInfo user = userInfoService.loadUserByUsername(loginRequest.getUsername());
    return new AuthResponse(token, user.getUsername(), user.getRole());
  }

  public AuthResponse register(RegisterRequest registerRequest) {
    if (registerRequest.getRole().equals("ADMIN")) {
      throw new IllegalArgumentException("Cannot register with ADMIN role");
    }

    UserInfo newUser = new UserInfo();
    newUser.setUserName(registerRequest.getUserName());
    newUser.setHashedPassword(passwordEncoder.encode(registerRequest.getPassword()));
    newUser.setDisplayName(registerRequest.getDisplayName());
    newUser.setEmail(registerRequest.getEmail());
    newUser.setPhoneNumber(registerRequest.getPhoneNumber());
    newUser.setRole(registerRequest.getRole());
    newUser.setBirthday(registerRequest.getBirthday());
    newUser.setGender(registerRequest.getGender());

    userInfoService.saveUser(newUser);
    String token = jwtService.generateJwtToken(newUser.getUsername());
    return new AuthResponse(token, newUser.getUsername(), newUser.getRole());
  }
}
