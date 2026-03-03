package com.ecommerce.controller;

import com.ecommerce.model.AddressInfo;
import com.ecommerce.model.Cart;
import com.ecommerce.model.OrderInfo;
import com.ecommerce.service.BuyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/buyer")
@RequiredArgsConstructor
public class BuyerController {
  private final BuyerService buyerService;

  @GetMapping("/orders")
  public ResponseEntity<List<OrderInfo>> getBuyerOrders(@AuthenticationPrincipal UserDetails user) {
    return ResponseEntity.ok(buyerService.getBuyerOrders(user.getUsername()));
  }

  @GetMapping("/addresses")
  public ResponseEntity<List<AddressInfo>> getBuyerAddresses(@AuthenticationPrincipal UserDetails user) {
    return ResponseEntity.ok(buyerService.getBuyerAddresses(user.getUsername()));
  }

  @GetMapping("/cart")
  public ResponseEntity<Cart> getBuyerCart(@AuthenticationPrincipal UserDetails user) {
    return ResponseEntity.ok(buyerService.getBuyerCart(user.getUsername()));
  }

  @PostMapping("/orders")
  public ResponseEntity<Map<String, String>> addOrderToBuyer(
          @AuthenticationPrincipal UserDetails user, OrderInfo orderInfo) {
    buyerService.addOrderToBuyer(user.getUsername(), orderInfo);
    return ResponseEntity.ok(Map.of("message", "Order added successfully"));
  }

  @PostMapping("/addresses")
  public ResponseEntity<Map<String, String>> addAddressToBuyer(
          @AuthenticationPrincipal UserDetails user, AddressInfo addressInfo) {
    buyerService.addAddressToBuyer(user.getUsername(), addressInfo);
    return ResponseEntity.ok(Map.of("message", "Address added successfully"));
  }

  @DeleteMapping("/addresses/{addressId}")
  public ResponseEntity<Map<String, String>> removeAddressFromBuyer(
          @AuthenticationPrincipal UserDetails user, @PathVariable String addressId) {
    buyerService.removeAddressFromBuyer(user.getUsername(), addressId);
    return ResponseEntity.ok(Map.of("message", "Address removed successfully"));
  }
}
