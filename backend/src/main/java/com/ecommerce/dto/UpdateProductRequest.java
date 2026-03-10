package com.ecommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UpdateProductRequest {
  @NotBlank(message = "Product name is required")
  private String productName;
  
  private String productBrand;
  
  @NotBlank(message = "Product category is required")
  private String productCategory;
  
  private String productDescription;
  
  @NotBlank(message = "Product origin is required")
  private String productMadeIn;
  
  @NotBlank(message = "Image URL is required")
  private String productImageUrl;
  
  @Valid
  @NotEmpty(message = "At least one SKU is required")
  private List<UpdateSkuRequest> skus;
}
