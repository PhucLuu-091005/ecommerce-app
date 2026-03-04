package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Buyer")
public class Buyer {
  @Id
  private String userName;

  @OneToOne
  @MapsId
  @JoinColumn(name = "userName")
  private UserInfo userInfo;

  @Column(nullable = false)
  Long moneySpent = 0L;

  @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderInfo> myOrders;

  @OneToOne(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
  private Cart cart;
}
