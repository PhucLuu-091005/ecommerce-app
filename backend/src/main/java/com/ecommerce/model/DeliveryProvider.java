package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "DeliveryProvider")
public class DeliveryProvider {

  @Id
  @Column(length = 100)
  private String providerName;
}
