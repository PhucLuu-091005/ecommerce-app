package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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

  @Column(nullable = false)
  private Double percentageDiscount = 0.0;

  @Column(nullable = false)
  private Integer maxAmountAllowed = 1;
}
