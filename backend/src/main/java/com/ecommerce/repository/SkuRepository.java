package com.ecommerce.repository;

import com.ecommerce.model.Sku;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkuRepository extends JpaRepository<Sku, Long> {
  List<Sku> findByProductInfo_ProductId(Long productId);
}

