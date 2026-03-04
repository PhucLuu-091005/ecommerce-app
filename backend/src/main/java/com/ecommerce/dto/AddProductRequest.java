package com.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddProductRequest {
  private String productName;
  private String productBrand;
  private String productCategory;
  private String productDescription;
  private String productMadeIn;
  private String imageUrl;
}

