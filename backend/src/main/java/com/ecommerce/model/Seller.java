package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Seller")
public class Seller {
  @Id
  private String userName;

  @OneToOne
  @MapsId
  @JoinColumn(name = "userName")
  private UserInfo userInfo;

  @Column(nullable = false, unique = true, length = 100)
  private String shopName;

  @Column(nullable = false, unique = true, length = 30)
  private String citizenIDCard;

  @Column(nullable = false, unique = true, length = 50)
  private String sellerName;

  @Column(nullable = false)
  private Long moneyEarned = 0L;

  @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<ProductInfo> productInfos;
}
