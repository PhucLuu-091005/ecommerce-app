package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "ProvideDelivery", uniqueConstraints = {
    @UniqueConstraint(name = "uq_provide_delivery", columnNames = {"providerName", "methodName"})
})
public class ProvideDelivery {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "providerName", nullable = false)
  private DeliveryProvider deliveryProvider;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "methodName", nullable = false)
  private DeliveryMethod deliveryMethod;
}
