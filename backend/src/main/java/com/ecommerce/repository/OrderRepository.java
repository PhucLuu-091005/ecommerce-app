package com.ecommerce.repository;

import com.ecommerce.model.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderInfo, Long> {
  List<OrderInfo> findByBuyer_UserInfo_UserName(String username);
  Optional<OrderInfo> findByOrderIdAndBuyer_UserInfo_UserName(Long orderId, String username);
}

