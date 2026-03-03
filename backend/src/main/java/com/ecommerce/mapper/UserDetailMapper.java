package com.ecommerce.mapper;

import com.ecommerce.dto.UserInfoDto;
import com.ecommerce.model.UserInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDetailMapper {
  UserInfoDto toDto(UserInfo userInfo);
  UserInfo toEntity(UserInfoDto userInfoDto);
}
