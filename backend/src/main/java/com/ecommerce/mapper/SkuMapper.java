package com.ecommerce.mapper;

import com.ecommerce.dto.SkuDto;
import com.ecommerce.model.Sku;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SkuMapper {
  SkuDto toDto(Sku sku);
}

