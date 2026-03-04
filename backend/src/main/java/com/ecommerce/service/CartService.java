package com.ecommerce.service;

import com.ecommerce.dto.CartDto;
import com.ecommerce.dto.CartItemRequest;
import com.ecommerce.dto.StoredSkuDto;
import com.ecommerce.model.Buyer;
import com.ecommerce.model.Cart;
import com.ecommerce.model.Sku;
import com.ecommerce.model.StoredSku;
import com.ecommerce.repository.BuyerRepository;
import com.ecommerce.repository.SkuRepository;
import com.ecommerce.repository.StoredSkuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
  private final BuyerRepository buyerRepository;
  private final SkuRepository skuRepository;
  private final StoredSkuRepository storedSkuRepository;

  @Transactional(readOnly = true)
  public CartDto getCart(String username) {
    Cart cart = getBuyer(username).getCart();

    List<StoredSkuDto> items = storedSkuRepository.findByCart_CartId(cart.getCartId())
        .stream()
        .map(stored -> {
          Sku sku = stored.getSku();
          StoredSkuDto dto = new StoredSkuDto();
          dto.setSkuId(sku.getId());
          dto.setSkuName(sku.getSkuName());
          dto.setSize(sku.getSize());
          dto.setPrice(sku.getPrice());
          dto.setInStockNumber(sku.getInStockNumber());
          dto.setWeight(sku.getWeight());
          dto.setImageUrl(sku.getImageUrl());
          dto.setQuantity(stored.getQuantity());
          return dto;
        })
        .toList();

    CartDto cartDto = new CartDto();
    cartDto.setCartId(cart.getCartId());
    cartDto.setTotalCost(cart.getTotalCost());
    cartDto.setItems(items);
    return cartDto;
  }

  @Transactional
  public void addOrUpdateItem(String username, CartItemRequest request) {
    if (request.getQuantity() <= 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be at least 1");
    }

    Cart cart = getBuyer(username).getCart();
    Sku sku = skuRepository.findById(request.getSkuId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SKU not found"));

    storedSkuRepository.findByCart_CartIdAndSku_Id(cart.getCartId(), sku.getId())
        .ifPresentOrElse(
            stored -> {
              stored.setQuantity(request.getQuantity());
              storedSkuRepository.save(stored);
            },
            () -> {
              StoredSku newItem = new StoredSku();
              newItem.setCart(cart);
              newItem.setSku(sku);
              newItem.setQuantity(request.getQuantity());
              storedSkuRepository.save(newItem);
            }
        );
  }

  @Transactional
  public void removeItem(String username, Long skuId) {
    Cart cart = getBuyer(username).getCart();
    storedSkuRepository.deleteByCart_CartIdAndSku_Id(cart.getCartId(), skuId);
  }

  private Buyer getBuyer(String username) {
    return buyerRepository.findById(username)
            .orElseThrow(() -> new UsernameNotFoundException("No buyer found: " + username));
  }
}
