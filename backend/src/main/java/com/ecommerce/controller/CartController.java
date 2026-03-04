package com.ecommerce.controller;

import com.ecommerce.dto.CartDto;
import com.ecommerce.dto.CartItemRequest;
import com.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
  private final CartService cartService;

  @GetMapping
  public ResponseEntity<CartDto> getMyCart(@AuthenticationPrincipal UserDetails user) {
    return ResponseEntity.ok(cartService.getCart(user.getUsername()));
  }

  @PostMapping
  public ResponseEntity<Map<String, String>> addOrUpdateItem(
      @AuthenticationPrincipal UserDetails user,
      @RequestBody CartItemRequest request) {
    cartService.addOrUpdateItem(user.getUsername(), request);
    return ResponseEntity.ok(Map.of("message", "Cart updated successfully"));
  }

  @DeleteMapping
  public ResponseEntity<Map<String, String>> removeItem(
      @AuthenticationPrincipal UserDetails user,
      @RequestBody CartItemRequest request) {
    cartService.removeItem(user.getUsername(), request.getSkuId());
    return ResponseEntity.ok(Map.of("message", "Item removed from cart"));
  }
}
