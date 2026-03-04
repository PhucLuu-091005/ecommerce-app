package com.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StoredSkuDto {
  private Long skuId;
  private String skuName;
  private Integer size;
  private Integer price;
  private Integer inStockNumber;
  private Integer weight;
  private String imageUrl;
  private Integer quantity;
}

