package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "StoredSKU", uniqueConstraints = {
    @UniqueConstraint(name = "uq_stored_sku", columnNames = {"cartId", "skuId"})
})
public class StoredSku {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cartId", nullable = false)
  private Cart cart;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "skuId", nullable = false)
  private Sku sku;

  @Column(nullable = false)
  private Integer quantity = 0;
}
