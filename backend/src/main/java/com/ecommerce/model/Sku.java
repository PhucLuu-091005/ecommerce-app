package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "SKU", uniqueConstraints = {
    @UniqueConstraint(name = "uq_sku", columnNames = {"productId", "skuName"})
})
public class Sku {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "productId", nullable = false)
  private ProductInfo productInfo;

  @Column(nullable = false, length = 100)
  private String skuName;

  private Integer size;

  @Column(nullable = false)
  private Integer price;

  @Column(nullable = false)
  private Integer inStockNumber = 0;

  private Integer weight;

  @Column(length = 200)
  private String imageUrl;
}
