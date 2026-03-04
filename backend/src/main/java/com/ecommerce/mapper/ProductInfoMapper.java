package com.ecommerce.mapper;

import com.ecommerce.dto.ProductInfoDto;
import com.ecommerce.model.ProductInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductInfoMapper {
  @Mapping(target = "skus", ignore = true)
  ProductInfoDto toDto(ProductInfo productInfo);
}


