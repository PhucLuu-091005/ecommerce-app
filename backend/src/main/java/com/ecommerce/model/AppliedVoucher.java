package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "AppliedVoucher", uniqueConstraints = {
    @UniqueConstraint(name = "uq_applied_voucher", columnNames = {"subOrderId", "voucherId"})
})
public class AppliedVoucher {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "subOrderId", nullable = false)
  private SubOrderInfo subOrder;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "voucherId", nullable = false)
  private Voucher voucher;
}
