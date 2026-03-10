package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Entity
@NoArgsConstructor
@Table(name = "ProductInfo")
public class ProductInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long productId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userName", nullable = false)
  private Seller seller;

  @Column(length = 100, nullable = false)
  private String productName;
  @Column(length = 100)
  private String productBrand;
  @Column(nullable = false)
  private String productCategory;
  private String productDescription;
  @Column(nullable = false)
  private String productMadeIn;
  @Column(nullable = false)
  private String productImageUrl;

  @OneToMany(mappedBy = "productInfo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Sku> skus;
}
