package com.ecommerce.controller;

import com.ecommerce.dto.CurrentUserDto;
import com.ecommerce.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class CurrentUserController {
  private final CurrentUserService currentUserService;

  @GetMapping
  public ResponseEntity<CurrentUserDto> getCurrentUser(@AuthenticationPrincipal UserDetails user) {
    return ResponseEntity.ok(currentUserService.getCurrentUser(user.getUsername()));
  }
}
