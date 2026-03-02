package com.ecommerce.controller;

import com.ecommerce.dto.LoginRequest;
//import com.ecommerce.security.JwtService;
import com.ecommerce.mapper.UserDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
//  private final UserService userService;
//  private final JwtService jwtService;
  private final UserDetailMapper mapper;

  @PostMapping("/register")
  public String register() {
    // Implement user registration logic here
    return "User registered successfully";
  }

  @PostMapping("/login")
  public String login(@RequestBody LoginRequest loginRequest) {
    // Implement user login logic here
    return "User logged in successfully";
  }

}
