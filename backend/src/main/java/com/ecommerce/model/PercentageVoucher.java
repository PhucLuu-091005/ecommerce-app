package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@Table(name = "PercentageVoucher")
public class PercentageVoucher {

  @Id
  @OneToOne
  @MapsId
  @JoinColumn(name = "voucherId")
  private Voucher voucher;

  @Column(nullable = false, precision = 5, scale = 4)
  private BigDecimal percentageDiscount = BigDecimal.ZERO;

  @Column(nullable = false)
  private Integer maxAmountAllowed = 1;
}
