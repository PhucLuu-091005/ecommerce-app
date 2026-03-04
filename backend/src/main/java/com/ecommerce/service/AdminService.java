package com.ecommerce.service;

import com.ecommerce.model.Buyer;
import com.ecommerce.model.Seller;
import com.ecommerce.repository.BuyerRepository;
import com.ecommerce.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
  private final SellerRepository sellerRepository;
  private final BuyerRepository buyerRepository;

  @Transactional(readOnly = true)
  public List<Seller> getAllSellers() {
    return sellerRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Seller getSellerByUsername(String username) {
    return sellerRepository.findById(username)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));
  }

  @Transactional(readOnly = true)
  public List<Buyer> getAllBuyers() {
    return buyerRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Buyer getBuyerByUsername(String username) {
    return buyerRepository.findById(username)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Buyer not found"));
  }
}

