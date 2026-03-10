package com.ecommerce.service;

import com.ecommerce.dto.AddProductRequest;
import com.ecommerce.dto.ProductInfoDto;
import com.ecommerce.dto.UpdateProductRequest;
import com.ecommerce.dto.UpdateSkuRequest;
import com.ecommerce.mapper.ProductInfoMapper;
import com.ecommerce.mapper.SkuRequestMapper;
import com.ecommerce.model.ProductInfo;
import com.ecommerce.model.Seller;
import com.ecommerce.model.Sku;
import com.ecommerce.repository.ProductInfoRepository;
import com.ecommerce.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
  private final ProductInfoRepository productInfoRepository;
  private final SellerRepository sellerRepository;
  private final ProductInfoMapper productInfoMapper;
  private final SkuRequestMapper skuRequestMapper;

  @Transactional(readOnly = true)
  public List<ProductInfoDto> getProductsBySellerUsername(String sellerUsername) {
    return productInfoRepository.findBySeller_UserInfo_UserName(sellerUsername)
        .stream().map(productInfoMapper::toDto).toList();
  }

  @Transactional(readOnly = true)
  public List<ProductInfoDto> getAllProducts() {
    return productInfoRepository.findAll()
        .stream().map(productInfoMapper::toDto).toList();
  }

  @Transactional(readOnly = true)
  public ProductInfoDto getProductById(Long productId) {
    ProductInfo product = productInfoRepository.findById(productId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    return productInfoMapper.toDto(product);
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
    product.setProductImageUrl(request.getProductImageUrl());

    ProductInfo savedProduct = productInfoRepository.save(product);

    List<Sku> skus = request.getSkus().stream()
        .map(skuReq ->
          skuRequestMapper.toEntity(skuReq, savedProduct)
        ).toList();
    savedProduct.setSkus(skus);

    return productInfoMapper.toDto(savedProduct);
  }

  @Transactional
  public ProductInfoDto updateProduct(String sellerUsername, Long productId, UpdateProductRequest request) {
    ProductInfo product = productInfoRepository.findById(productId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

    if (!product.getSeller().getUserInfo().getUsername().equals(sellerUsername)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this product");
    }

    // Update product basic fields
    product.setProductName(request.getProductName());
    product.setProductBrand(request.getProductBrand());
    product.setProductCategory(request.getProductCategory());
    product.setProductDescription(request.getProductDescription());
    product.setProductMadeIn(request.getProductMadeIn());
    product.setProductImageUrl(request.getProductImageUrl());

    // Smart upsert for SKUs (request contains only SKUs to update or add, SKUs not in request will be kept unchanged)
    if (request.getSkus() != null && !request.getSkus().isEmpty()) {
      // Get current SKU IDs from product
      List<Sku> currentSkus = product.getSkus();
      if (currentSkus == null) {
        currentSkus = new ArrayList<>();
        product.setSkus(currentSkus);
      }

      // Process each SKU in the request
      for (UpdateSkuRequest skuReq : request.getSkus()) {
        // If SKU ID is present, update existing SKU
        if (skuReq.getSkuId() != null) {
          // Update existing SKU
          Sku existingSku = currentSkus.stream()
              .filter(s -> s.getId().equals(skuReq.getSkuId()))
              .findFirst()
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                  "SKU ID " + skuReq.getSkuId() + " not found in this product"));
          
          existingSku.setSkuName(skuReq.getSkuName());
          existingSku.setSize(skuReq.getSize());
          existingSku.setPrice(skuReq.getPrice());
          existingSku.setInStockNumber(skuReq.getInStockNumber());
          existingSku.setWeight(skuReq.getWeight());
          existingSku.setImageUrl(skuReq.getImageUrl());
        } else {
          // Create new SKU
          Sku newSku = new Sku();
          newSku.setProductInfo(product);
          newSku.setSkuName(skuReq.getSkuName());
          newSku.setSize(skuReq.getSize());
          newSku.setPrice(skuReq.getPrice());
          newSku.setInStockNumber(skuReq.getInStockNumber());
          newSku.setWeight(skuReq.getWeight());
          newSku.setImageUrl(skuReq.getImageUrl());
          currentSkus.add(newSku);
        }
      }
    }

    return productInfoMapper.toDto(productInfoRepository.save(product));
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
}
