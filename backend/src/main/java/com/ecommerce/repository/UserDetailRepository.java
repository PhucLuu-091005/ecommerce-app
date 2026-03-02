package com.ecommerce.repository;

import com.ecommerce.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserInfo, String> {}
