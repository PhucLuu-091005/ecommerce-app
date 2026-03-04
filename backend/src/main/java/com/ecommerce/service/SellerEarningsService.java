package com.ecommerce.service;

import com.ecommerce.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SellerEarningsService {
  private final SellerRepository sellerRepository;

  @Transactional(readOnly = true)
  public Long getEarnings(String sellerUsername) {
    return sellerRepository.findById(sellerUsername)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"))
        .getMoneyEarned();
  }
}

