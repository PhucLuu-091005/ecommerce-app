package com.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateSkuRequest {
  // If skuId is provided, update existing SKU; otherwise create new
  private Long skuId;
  
  @NotBlank(message = "SKU name is required")
  private String skuName;
  
  @Min(value = 0, message = "Size must be non-negative")
  private Integer size;
  
  @NotNull(message = "Price is required")
  @Min(value = 0, message = "Price must be non-negative")
  private Integer price;
  
  @NotNull(message = "Stock number is required")
  @Min(value = 0, message = "Stock number must be non-negative")
  private Integer inStockNumber;
  
  @Min(value = 0, message = "Weight must be non-negative")
  private Integer weight;
  
  private String imageUrl;
}
