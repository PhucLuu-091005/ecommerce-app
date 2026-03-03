package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "SubOrderDetail", uniqueConstraints = {
    @UniqueConstraint(name = "uq_sub_order_detail", columnNames = {"subOrderId", "skuId"})
})
public class SubOrderDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "subOrderId", nullable = false)
  private SubOrderInfo subOrder;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "skuId", nullable = false)
  private Sku sku;

  @Column(nullable = false)
  private Integer quantity = 0;
}
