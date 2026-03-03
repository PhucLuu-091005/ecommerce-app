package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Cart")
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long cartId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userName", nullable = false)
  private Buyer buyer;

  @Column(nullable = false)
  private Long totalCost = 0L;
}
