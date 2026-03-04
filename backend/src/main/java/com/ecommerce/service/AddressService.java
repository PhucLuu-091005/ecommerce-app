package com.ecommerce.service;

import com.ecommerce.model.AddressInfo;
import com.ecommerce.model.Buyer;
import com.ecommerce.repository.BuyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
  private final BuyerRepository buyerRepository;

  @Transactional(readOnly = true)
  public List<AddressInfo> getAddresses(String username) {
    return getBuyer(username).getUserInfo().getAddressInfos();
  }

  @Transactional
  public void addAddress(String username, AddressInfo addressInfo) {
    Buyer buyer = getBuyer(username);
    buyer.getUserInfo().getAddressInfos().add(addressInfo);
    buyerRepository.save(buyer);
  }

  @Transactional
  public void removeAddress(String username, Long addressId) {
    Buyer buyer = getBuyer(username);
    buyer.getUserInfo().getAddressInfos()
        .removeIf(address -> address.getId().equals(addressId));
    buyerRepository.save(buyer);
  }

  private Buyer getBuyer(String username) {
    return buyerRepository.findById(username)
        .orElseThrow(() -> new UsernameNotFoundException("No buyer found: " + username));
  }
}

