package com.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "AddressInfo")
public class AddressInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userName", nullable = false)
  private UserInfo userInfo;

  @Column(nullable = false)
  private String contactName;

  @Column(nullable = false)
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
