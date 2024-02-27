package com.example.splitbilleats.application.controller;

import com.example.splitbilleats.application.dto.OrderDto;
import com.example.splitbilleats.application.dto.PaymentSplitDto;
import com.example.splitbilleats.domain.service.OrderService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
  @Autowired
  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @CrossOrigin
  @PostMapping
  public ResponseEntity<PaymentSplitDto> createOrder(@RequestBody @Valid OrderDto orderDto) {
    try {
      PaymentSplitDto paymentSplited = orderService.calculateSplit(orderDto);
      return new ResponseEntity<>(paymentSplited, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}