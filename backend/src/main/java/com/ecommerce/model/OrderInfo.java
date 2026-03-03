package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "OrderInfo")
public class OrderInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userName", nullable = false)
  private Buyer buyer;

  @Column(nullable = false)
  private LocalDateTime orderDate;

  @Column(nullable = false)
  private Long totalPrice = 0L;

  @Column(nullable = false, length = 10)
  private String bankProviderName = "VCB"; // VCB | OCB | MoMo | ZaloPay

  @Column(length = 30)
  private String accountId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "addressId", nullable = false)
  private AddressInfo addressInfo;
}
