package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "SKUImage", uniqueConstraints = {
    // Each SKU must have unique image URLs to prevent duplicates
    @UniqueConstraint(name = "uq_sku_image", columnNames = {"skuId", "skuUrl"})
})
public class SkuImage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "skuId", nullable = false)
  private Sku sku;

  @Column(nullable = false, length = 200)
  private String skuUrl;
}
