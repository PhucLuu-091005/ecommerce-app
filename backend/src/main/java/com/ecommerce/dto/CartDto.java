package com.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CartDto {
  private Long cartId;
  private Long totalCost;
  private List<StoredSkuDto> items;
}

