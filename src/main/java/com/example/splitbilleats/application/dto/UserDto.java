package com.example.splitbilleats.application.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class UserDto {
  @NotNull(message = "Name cannot be null")
  private String name;
  @Valid
  private List<OrderItemDto> items;

  public UserDto() {
  }

  public UserDto(String name, List<OrderItemDto> items) {
    this.name = name;
    this.items = items;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<OrderItemDto> getItems() {
    return items;
  }

  public void setItems(List<OrderItemDto> items) {
    this.items = items;
  }
}
