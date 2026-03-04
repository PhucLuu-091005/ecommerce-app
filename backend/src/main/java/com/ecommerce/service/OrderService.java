package com.ecommerce.service;

import com.ecommerce.model.Buyer;
import com.ecommerce.model.OrderInfo;
import com.ecommerce.repository.BuyerRepository;
import com.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final BuyerRepository buyerRepository;
  private final OrderRepository orderRepository;

  @Transactional(readOnly = true)
  public List<OrderInfo> getBuyerOrders(String username) {
    return orderRepository.findByBuyer_UserInfo_UserName(username);
  }

  @Transactional(readOnly = true)
  public OrderInfo getOrderById(String username, Long orderId) {
    return orderRepository.findByOrderIdAndBuyer_UserInfo_UserName(orderId, username)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
  }

  @Transactional
  public void placeOrder(String username, OrderInfo orderInfo) {
    Buyer buyer = getBuyer(username);
    buyer.getMyOrders().add(orderInfo);
    buyerRepository.save(buyer);
  }

  @Transactional(readOnly = true)
  public Long getSpending(String username) {
    return getBuyer(username).getMoneySpent();
  }

  private Buyer getBuyer(String username) {
    return buyerRepository.findById(username)
        .orElseThrow(() -> new UsernameNotFoundException("No buyer found: " + username));
  }
}
