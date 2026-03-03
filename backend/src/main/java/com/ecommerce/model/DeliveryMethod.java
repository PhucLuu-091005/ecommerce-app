package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "DeliveryMethod")
public class DeliveryMethod {

  @Id
  @Column(length = 100)
  private String methodName;
}
