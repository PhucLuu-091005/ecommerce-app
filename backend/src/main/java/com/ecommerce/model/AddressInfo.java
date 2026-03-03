package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "AddressInfo", uniqueConstraints = {
    @UniqueConstraint(name = "uq_default_address", columnNames = {"userName", "isAddressDefault"})
})
public class AddressInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userName", nullable = false)
  private UserInfo userInfo;

  @Column(nullable = false)
  private String contactName;

  @Column(nullable = false, length = 10)
  private String contactPhoneNumber;

  @Column(nullable = false, length = 100)
  private String city;

  @Column(nullable = false, length = 100)
  private String district;

  @Column(nullable = false, length = 100)
  private String commune;

  @Column(nullable = false, length = 500)
  private String detailAddress;

  @Column(nullable = false, length = 50)
  private String addressType = "Home"; // Home | Office

  @Column(nullable = false, length = 1)
  private String isAddressDefault = "Y"; // Y | N
}
