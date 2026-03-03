package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "SubOrderInfo")
public class SubOrderInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "orderId", nullable = false)
  private OrderInfo order;

  @Column(nullable = false)
  private Long totalSkuPrice = 0L;

  @Column(nullable = false, length = 20)
  private String shippingStatus = "Preparing"; // Preparing | Shipping | Done | Cancelled

  @Column(nullable = false)
  private LocalDateTime actualDate;

  @Column(nullable = false)
  private LocalDateTime expectedDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "deliveryMethodName", nullable = false)
  private DeliveryMethod deliveryMethod;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "deliveryProviderName", nullable = false)
  private DeliveryProvider deliveryProvider;

  @Column(nullable = false)
  private Integer deliveryPrice = 0;
}
