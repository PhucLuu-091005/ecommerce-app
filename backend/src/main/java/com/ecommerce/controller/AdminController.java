package com.ecommerce.controller;

import com.ecommerce.model.Buyer;
import com.ecommerce.model.Seller;
import com.ecommerce.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
  private final AdminService adminService;

  @GetMapping("/sellers")
  public ResponseEntity<List<Seller>> getAllSellers() {
    return ResponseEntity.ok(adminService.getAllSellers());
  }

  @GetMapping("/sellers/{username}")
  public ResponseEntity<Seller> getSeller(@PathVariable String username) {
    return ResponseEntity.ok(adminService.getSellerByUsername(username));
  }

  @GetMapping("/buyers")
  public ResponseEntity<List<Buyer>> getAllBuyers() {
    return ResponseEntity.ok(adminService.getAllBuyers());
  }

  @GetMapping("/buyers/{username}")
  public ResponseEntity<Buyer> getBuyer(@PathVariable String username) {
    return ResponseEntity.ok(adminService.getBuyerByUsername(username));
  }
}

