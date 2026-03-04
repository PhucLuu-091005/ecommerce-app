package com.ecommerce.controller;

import com.ecommerce.dto.AddProductRequest;
import com.ecommerce.dto.ProductInfoDto;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.SellerEarningsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
  private final ProductService productService;
  private final SellerEarningsService sellerEarningsService;

  @GetMapping
  public ResponseEntity<List<ProductInfoDto>> getAllProducts() {
    return ResponseEntity.ok(productService.getAllProducts());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductInfoDto> getProductById(@PathVariable Long id) {
    return ResponseEntity.ok(productService.getProductById(id));
  }

  @GetMapping("/mine")
  public ResponseEntity<List<ProductInfoDto>> getMyProducts(@AuthenticationPrincipal UserDetails user) {
    return ResponseEntity.ok(productService.getProductsBySellerUsername(user.getUsername()));
  }

  @PostMapping
  public ResponseEntity<ProductInfoDto> addProduct(
      @AuthenticationPrincipal UserDetails user,
      @RequestBody AddProductRequest request) {
    return ResponseEntity.ok(productService.addProduct(user.getUsername(), request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductInfoDto> updateProduct(
      @AuthenticationPrincipal UserDetails user,
      @PathVariable Long id,
      @RequestBody AddProductRequest request) {
    return ResponseEntity.ok(productService.updateProduct(user.getUsername(), id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, String>> deleteProduct(
      @AuthenticationPrincipal UserDetails user,
      @PathVariable Long id) {
    productService.deleteProduct(user.getUsername(), id);
    return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
  }

  @GetMapping("/earnings")
  public ResponseEntity<Map<String, Long>> getEarnings(@AuthenticationPrincipal UserDetails user) {
    return ResponseEntity.ok(Map.of("earnings", sellerEarningsService.getEarnings(user.getUsername())));
  }
}
