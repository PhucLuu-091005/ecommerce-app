package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "VoucherOffer", uniqueConstraints = {
    @UniqueConstraint(name = "uq_voucher_offer", columnNames = {"voucherId", "productId"})
})
public class VoucherOffer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "voucherId", nullable = false)
  private Voucher voucher;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "productId", nullable = false)
  private ProductInfo productInfo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userName", nullable = false)
  private UserInfo userInfo;
}
