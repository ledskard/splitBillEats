package com.example.splitbilleats.application.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Max;

public class OrderDto {
  private Long id;

  @NotNull(message = "Users list cannot be null")
  @Valid
  @Size(min = 1, message = "At least one user must be provided")
  private List<UserDto> users;

  @Min(value = 0, message = "totalBeforeDiscountsAndFees must be non-negative")
  private Double totalBeforeDiscountsAndFees;

  @Min(value = 0, message = "discount must be non-negative")
  private Double discount;

  @Min(value = 0, message = "deliveryFee must be non-negative")
  private Double deliveryFee;

  @Min(value = 0, message = "total must be non-negative")
  private Double total;

  @Min(value = 0, message = "Discount percentage must be non-negative")
  @Max(value = 100, message = "Discount percentage cannot be more than 100")
  private Double discountPercentage;

  @Min(value = 0, message = "Service charge percentage must be non-negative")
  @Max(value = 100, message = "Service charge percentage cannot be more than 100")
  private Double serviceChargePercentage;

  public OrderDto() {
  }

  public OrderDto(Long id, List<UserDto> users, Double discount, Double deliveryFee, Double discountPercentage,
      Double serviceChargePercentage) {
    this.id = id;
    this.users = users;
    this.discount = discount;
    this.deliveryFee = deliveryFee;
    this.discountPercentage = discountPercentage;
    this.serviceChargePercentage = serviceChargePercentage;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<UserDto> getUsers() {
    return users;
  }

  public void setUsers(List<UserDto> users) {
    this.users = users;
  }

  public Double getTotalBeforeDiscountsAndFees() {
    return totalBeforeDiscountsAndFees;
  }

  public void setTotalBeforeDiscountsAndFees(Double totalBeforeDiscountsAndFees) {
    this.totalBeforeDiscountsAndFees = totalBeforeDiscountsAndFees;
  }

  public Double getDiscount() {
    return discount;
  }

  public void setDiscount(Double discount) {
    this.discount = discount;
  }

  public Double getDeliveryFee() {
    return deliveryFee;
  }

  public void setDeliveryFee(Double deliveryFee) {
    this.deliveryFee = deliveryFee;
  }

  public Double getTotal() {
    return total;
  }

  public void setTotal(Double total) {
    this.total = total;
  }

  public Double getDiscountPercentage() {
    return discountPercentage;
  }

  public void setDiscountPercentage(Double discountPercentage) {
    this.discountPercentage = discountPercentage;
  }

  public Double getServiceChargePercentage() {
    return serviceChargePercentage;
  }

  public void setServiceChargePercentage(Double serviceChargePercentage) {
    this.serviceChargePercentage = serviceChargePercentage;
  }
}
