package com.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductInfoDto {
  private Long productId;
  private String productName;
  private String productBrand;
  private String productCategory;
  private String productDescription;
  private String productMadeIn;
  private String productImageUrl;
  private List<SkuDto> skus;
}

