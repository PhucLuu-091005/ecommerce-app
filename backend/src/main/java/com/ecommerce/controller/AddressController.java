package com.ecommerce.controller;

import com.ecommerce.model.AddressInfo;
import com.ecommerce.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {
  private final AddressService addressService;

  @GetMapping
  public ResponseEntity<List<AddressInfo>> getMyAddresses(@AuthenticationPrincipal UserDetails user) {
    return ResponseEntity.ok(addressService.getAddresses(user.getUsername()));
  }

  @PostMapping
  public ResponseEntity<Map<String, String>> addAddress(
      @AuthenticationPrincipal UserDetails user,
      @RequestBody AddressInfo addressInfo) {
    addressService.addAddress(user.getUsername(), addressInfo);
    return ResponseEntity.ok(Map.of("message", "Address added successfully"));
  }

  @DeleteMapping("/{addressId}")
  public ResponseEntity<Map<String, String>> removeAddress(
      @AuthenticationPrincipal UserDetails user,
      @PathVariable Long addressId) {
    addressService.removeAddress(user.getUsername(), addressId);
    return ResponseEntity.ok(Map.of("message", "Address removed successfully"));
  }
}

