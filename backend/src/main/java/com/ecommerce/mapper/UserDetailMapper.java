package com.ecommerce.mapper;

import com.ecommerce.dto.UserDetailDto;
import com.ecommerce.model.UserInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDetailMapper {
  UserDetailDto toDto(UserInfo userInfo);
  UserInfo toEntity(UserDetailDto userDetailDto);
}
