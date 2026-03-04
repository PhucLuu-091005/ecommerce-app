package com.ecommerce.service;

import com.ecommerce.dto.AuthResponse;
import com.ecommerce.dto.LoginRequest;
import com.ecommerce.dto.RegisterRequest;
import com.ecommerce.model.Buyer;
import com.ecommerce.model.Cart;
import com.ecommerce.model.Seller;
import com.ecommerce.model.UserInfo;
import com.ecommerce.repository.BuyerRepository;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserInfoService userInfoService;
  private final PasswordEncoder passwordEncoder;
  private final BuyerRepository buyerRepository;
  private final SellerRepository sellerRepository;
  private final CartRepository cartRepository;

  public AuthResponse login(LoginRequest loginRequest) {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
    );
    String token = jwtService.generateJwtToken(loginRequest.getUsername());
    UserInfo user = userInfoService.loadUserByUsername(loginRequest.getUsername());
    return new AuthResponse(token, user.getUsername(), user.getRole());
  }

  @Transactional
  public AuthResponse register(RegisterRequest req) {
    if ("ADMIN".equals(req.getRole())) {
      throw new IllegalArgumentException("Cannot register with ADMIN role");
    }

    // 1. Create and persist UserInfo
    UserInfo newUser = new UserInfo();
    newUser.setUserName(req.getUserName());
    newUser.setHashedPassword(passwordEncoder.encode(req.getPassword()));
    newUser.setDisplayName(req.getDisplayName());
    newUser.setEmail(req.getEmail());
    newUser.setPhoneNumber(req.getPhoneNumber());
    newUser.setRole(req.getRole());
    newUser.setBirthDate(req.getBirthday() != null ? LocalDate.parse(req.getBirthday()) : null);
    newUser.setGender(req.getGender());
    UserInfo savedUser = userInfoService.saveUser(newUser);

    // 2. Create role-specific profile
    if ("BUYER".equals(req.getRole())) {
      Buyer buyer = new Buyer();
      buyer.setUserInfo(savedUser);    // @MapsId derives PK from userInfo.userName
      buyerRepository.save(buyer);

      Cart cart = new Cart();
      cart.setBuyer(buyer);
      cartRepository.save(cart);

    } else if ("SELLER".equals(req.getRole())) {
      if (req.getShopName() == null || req.getCitizenIDCard() == null || req.getSellerName() == null) {
        throw new IllegalArgumentException("shopName, citizenIDCard, and sellerName are required for SELLER registration");
      }
      Seller seller = new Seller();
      seller.setUserInfo(savedUser);   // @MapsId derives PK from userInfo.userName
      seller.setShopName(req.getShopName());
      seller.setCitizenIDCard(req.getCitizenIDCard());
      seller.setSellerName(req.getSellerName());
      sellerRepository.save(seller);
    }

    // 3. Generate and return JWT (same flow as login)
    String token = jwtService.generateJwtToken(savedUser.getUsername());
    return new AuthResponse(token, savedUser.getUsername(), savedUser.getRole());
  }
}
