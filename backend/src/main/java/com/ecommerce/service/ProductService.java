package com.ecommerce.service;

import com.ecommerce.dto.AddProductRequest;
import com.ecommerce.dto.ProductInfoDto;
import com.ecommerce.dto.SkuDto;
import com.ecommerce.mapper.ProductInfoMapper;
import com.ecommerce.mapper.SkuMapper;
import com.ecommerce.model.ProductInfo;
import com.ecommerce.model.Seller;
import com.ecommerce.repository.ProductInfoRepository;
import com.ecommerce.repository.SellerRepository;
import com.ecommerce.repository.SkuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
  private final ProductInfoRepository productInfoRepository;
  private final SkuRepository skuRepository;
  private final SellerRepository sellerRepository;
  private final ProductInfoMapper productInfoMapper;
  private final SkuMapper skuMapper;

  @Transactional(readOnly = true)
  public List<ProductInfoDto> getProductsBySellerUsername(String sellerUsername) {
    return productInfoRepository.findBySeller_UserInfo_UserName(sellerUsername)
        .stream().map(this::toDto).toList();
  }

  @Transactional(readOnly = true)
  public List<ProductInfoDto> getAllProducts() {
    return productInfoRepository.findAll()
        .stream().map(this::toDto).toList();
  }

  @Transactional(readOnly = true)
  public ProductInfoDto getProductById(Long productId) {
    ProductInfo product = productInfoRepository.findById(productId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    return toDto(product);
  }

  @Transactional
  public ProductInfoDto addProduct(String sellerUsername, AddProductRequest request) {
    Seller seller = sellerRepository.findById(sellerUsername)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));

    ProductInfo product = new ProductInfo();
    product.setSeller(seller);
    product.setProductName(request.getProductName());
    product.setProductBrand(request.getProductBrand());
    product.setProductCategory(request.getProductCategory());
    product.setProductDescription(request.getProductDescription());
    product.setProductMadeIn(request.getProductMadeIn());
    product.setProductImageUrl(request.getImageUrl());

    return toDto(productInfoRepository.save(product));
  }

  @Transactional
  public ProductInfoDto updateProduct(String sellerUsername, Long productId, AddProductRequest request) {
    ProductInfo product = productInfoRepository.findById(productId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

    if (!product.getSeller().getUserInfo().getUsername().equals(sellerUsername)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this product");
    }

    if (request.getProductName() != null)        product.setProductName(request.getProductName());
    if (request.getProductBrand() != null)       product.setProductBrand(request.getProductBrand());
    if (request.getProductCategory() != null)    product.setProductCategory(request.getProductCategory());
    if (request.getProductDescription() != null) product.setProductDescription(request.getProductDescription());
    if (request.getProductMadeIn() != null)      product.setProductMadeIn(request.getProductMadeIn());
    if (request.getImageUrl() != null)           product.setProductImageUrl(request.getImageUrl());

    return toDto(productInfoRepository.save(product));
  }

  @Transactional
  public void deleteProduct(String sellerUsername, Long productId) {
    ProductInfo product = productInfoRepository.findById(productId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

    if (!product.getSeller().getUserInfo().getUsername().equals(sellerUsername)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this product");
    }

    productInfoRepository.delete(product);
  }

  // Helper
  private ProductInfoDto toDto(ProductInfo product) {
    ProductInfoDto dto = productInfoMapper.toDto(product);
    List<SkuDto> skus = skuRepository.findByProductInfo_ProductId(product.getProductId())
        .stream().map(skuMapper::toDto).toList();
    dto.setSkus(skus);
    return dto;
  }
}
