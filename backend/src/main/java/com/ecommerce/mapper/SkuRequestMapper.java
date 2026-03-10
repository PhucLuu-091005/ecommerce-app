package com.ecommerce.mapper;

import com.ecommerce.dto.SkuInAddProductRequest;
import com.ecommerce.model.ProductInfo;
import com.ecommerce.model.Sku;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SkuRequestMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "productInfo", source = "productInfo")
  Sku toEntity(SkuInAddProductRequest request, ProductInfo productInfo);
}
