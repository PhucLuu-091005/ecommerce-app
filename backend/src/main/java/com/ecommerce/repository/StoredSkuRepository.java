package com.ecommerce.repository;

import com.ecommerce.model.StoredSku;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoredSkuRepository extends JpaRepository<StoredSku, Long> {
  List<StoredSku> findByCart_CartId(Long cartId);
  Optional<StoredSku> findByCart_CartIdAndSku_Id(Long cartId, Long skuId);
  void deleteByCart_CartIdAndSku_Id(Long cartId, Long skuId);
}

