package com.ecommerce.repository;

import com.ecommerce.model.ProductInfo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductInfoRepository extends JpaRepository<ProductInfo, Long> {
  @EntityGraph(attributePaths = {"skus"})
  List<ProductInfo> findBySeller_UserInfo_UserName(String sellerUsername);
  
  @EntityGraph(attributePaths = {"skus"})
  @Override
  Optional<ProductInfo> findById(Long id);
  
  @EntityGraph(attributePaths = {"skus"})
  @Override
  List<ProductInfo> findAll();
}
