package com.ecommerce.service;

import com.ecommerce.dto.CurrentUserDto;
import com.ecommerce.model.UserInfo;
import com.ecommerce.repository.BuyerRepository;
import com.ecommerce.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CurrentUserService {
  private final UserInfoService userInfoService;
  private final BuyerRepository buyerRepository;
  private final SellerRepository sellerRepository;

  @Transactional(readOnly = true)
  public CurrentUserDto getCurrentUser(String username) {
    UserInfo userInfo = userInfoService.loadUserByUsername(username);

    CurrentUserDto dto = new CurrentUserDto();
    dto.setUsername(userInfo.getUsername());
    dto.setEmail(userInfo.getEmail());
    dto.setPhoneNumber(userInfo.getPhoneNumber());
    dto.setRole(userInfo.getRole());

    // Check if user is a seller
    sellerRepository.findById(username).ifPresent(seller -> {
      dto.setShopName(seller.getShopName());
      dto.setEarnings(seller.getMoneyEarned());
    });

    // Check if user is a buyer
    buyerRepository.findById(username).ifPresent(buyer ->
      dto.setSpending(buyer.getMoneySpent())
    );

    return dto;
  }
}
