package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Seller")
public class Seller {

  @Id
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
}
