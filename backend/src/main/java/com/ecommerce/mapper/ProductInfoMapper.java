package com.ecommerce.mapper;

import com.ecommerce.dto.ProductInfoDto;
import com.ecommerce.model.ProductInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SkuMapper.class})
public interface ProductInfoMapper {
  ProductInfoDto toDto(ProductInfo productInfo);
}


