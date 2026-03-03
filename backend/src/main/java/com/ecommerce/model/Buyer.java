package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
public class Buyer {
  @OneToOne
  @MapsId
  @JoinColumn(name = "userName")
  @Id
  private UserInfo userInfo;

  @Column(nullable = false)
  BigDecimal moneySpent;

  @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderInfo> myOrders;

  @OneToOne(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
  private Cart cart;
}
