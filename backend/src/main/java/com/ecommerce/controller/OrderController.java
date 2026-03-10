package com.ecommerce.controller;

import com.ecommerce.model.OrderInfo;
import com.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
  private final OrderService orderService;

  @GetMapping
  public ResponseEntity<List<OrderInfo>> getMyOrders(@AuthenticationPrincipal UserDetails user) {
    return ResponseEntity.ok(orderService.getBuyerOrders(user.getUsername()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderInfo> getOrderById(
      @AuthenticationPrincipal UserDetails user,
      @PathVariable Long id) {
    return ResponseEntity.ok(orderService.getOrderById(user.getUsername(), id));
  }

  @PostMapping
  public ResponseEntity<Map<String, String>> placeOrder(
      @AuthenticationPrincipal UserDetails user,
      @RequestBody OrderInfo orderInfo) {
    orderService.placeOrder(user.getUsername(), orderInfo);
    return ResponseEntity.ok(Map.of("message", "Order placed successfully"));
  }
}
