package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Withdrawal")
public class Withdrawal {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long withdrawalId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userName", nullable = false)
  private UserInfo userInfo;

  @Column(nullable = false)
  private Integer withdrawalAmount;

  @Column(nullable = false)
  private LocalDateTime withdrawalTime;

  @Column(length = 30, unique = true)
  private String accountId;

  @Column(nullable = false, length = 20)
  private String providerName = "VCB"; // VCB | MoMo | OCB | ZaloPay

  @Column(nullable = false)
  private Integer remainingBalance = 0;
}
