package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "DeliveryPartner", uniqueConstraints = {
    @UniqueConstraint(name = "uq_delivery_partner", columnNames = {"userName", "providerName"})
})
public class DeliveryPartner {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userName", nullable = false)
  private UserInfo userInfo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "providerName", nullable = false)
  private DeliveryProvider deliveryProvider;
}
