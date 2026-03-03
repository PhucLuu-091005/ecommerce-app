package com.ecommerce.service;

import com.ecommerce.model.AddressInfo;
import com.ecommerce.model.Buyer;
import com.ecommerce.model.Cart;
import com.ecommerce.model.OrderInfo;
import com.ecommerce.repository.BuyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuyerService {
  private final BuyerRepository buyerRepository;

  public List<OrderInfo> getBuyerOrders(String userName) {
    return this.getBuyerByUsername(userName).getMyOrders();
  }

  public List<AddressInfo> getBuyerAddresses(String userName) {
    return this.getBuyerByUsername(userName)
            .getUserInfo().getAddressInfos();
  }

  public Cart getBuyerCart(String userName) {
    return this.getBuyerByUsername(userName).getCart();
  }

  public void addOrderToBuyer(String userName, OrderInfo orderInfo) {
    Buyer buyer = this.getBuyerByUsername(userName);
    buyer.getMyOrders().add(orderInfo);
    buyerRepository.save(buyer);
  }

  public void addAddressToBuyer(String userName, AddressInfo addressInfo) {
    Buyer buyer = this.getBuyerByUsername(userName);
    buyer.getUserInfo().getAddressInfos().add(addressInfo);
    buyerRepository.save(buyer);
  }

  public void removeAddressFromBuyer(String userName, String addressId) {
    Buyer buyer = this.getBuyerByUsername(userName);
    buyer.getUserInfo().getAddressInfos().removeIf(
            address -> address.getId() == Long.parseLong(addressId)
            );
    buyerRepository.save(buyer);
  }

  private Buyer getBuyerByUsername(String userName) {
    return buyerRepository.findById(userName)
        .orElseThrow(() -> new UsernameNotFoundException("No buyer found with username: " + userName));
  }
}
