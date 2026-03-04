package com.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SkuDto {
  private Long skuId;
  private String skuName;
  private Integer size;
  private Integer price;
  private Integer inStockNumber;
  private Integer weight;
  private String imageUrl;
}

