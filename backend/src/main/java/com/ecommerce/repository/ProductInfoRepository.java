package com.ecommerce.repository;

import com.ecommerce.model.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductInfoRepository extends JpaRepository<ProductInfo, Long> {
  List<ProductInfo> findBySeller_UserInfo_UserName(String sellerUsername);
}
