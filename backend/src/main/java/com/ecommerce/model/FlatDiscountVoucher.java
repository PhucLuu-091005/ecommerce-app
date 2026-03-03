package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "FlatDiscountVoucher")
public class FlatDiscountVoucher {

  @Id
  @OneToOne
  @MapsId
  @JoinColumn(name = "voucherId")
  private Voucher voucher;

  @Column(nullable = false)
  private Integer discountAmount = 0;
}
