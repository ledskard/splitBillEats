package com.example.splitbilleats.domain.service;

import com.example.splitbilleats.application.dto.OrderDto;
import com.example.splitbilleats.application.dto.OrderItemDto;
import com.example.splitbilleats.application.dto.PaymentSplitDto;
import com.example.splitbilleats.application.dto.ParticipantPayment;
import com.example.splitbilleats.infrastructure.payment.interfaces.PaymentGateway;
import com.example.splitbilleats.application.dto.UserDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
  @Autowired
  private final PaymentGateway paymentGateway;

  public OrderService(PaymentGateway paymentGateway) {
    this.paymentGateway = paymentGateway;
  }

  public PaymentSplitDto calculateSplit(OrderDto orderDto) {
    double totalBefore = calculateTotalBeforeDiscounts(orderDto.getUsers());
    double totalDiscounts = calculateTotalDiscounts(orderDto, totalBefore);
    double totalServiceCharges = calculateTotalServiceCharges(orderDto, totalBefore);
    double totalAfterDiscountsAndCharges = calculateTotalAfterDiscountsAndCharges(orderDto, totalBefore, totalDiscounts,
        totalServiceCharges);

    List<ParticipantPayment> participantPayments = orderDto.getUsers().stream()
        .map(user -> {
          double userTotalBefore = user.getItems().stream()
              .mapToDouble(item -> item.getPrice() * item.getQuantity())
              .sum();

          List<String> itemNames = user.getItems().stream()
              .map(OrderItemDto::getName)
              .collect(Collectors.toList());

          double userAmountDue = calculateUserAmountDue(userTotalBefore, totalBefore, totalDiscounts,
              totalServiceCharges, orderDto.getDeliveryFee());
          return new ParticipantPayment(user.getName(), userAmountDue, itemNames,
              paymentGateway.generatePaymentLink(userAmountDue));
        })
        .collect(Collectors.toList());

    return new PaymentSplitDto(totalBefore, totalAfterDiscountsAndCharges, participantPayments);
  }

  private static double calculateTotalBeforeDiscounts(List<UserDto> users) {
    return users.stream()
        .flatMap(user -> user.getItems().stream())
        .mapToDouble(item -> item.getPrice() * item.getQuantity())
        .sum();
  }

  private double calculateUserAmountDue(double userTotalBefore, double totalBefore, double totalDiscounts,
      double totalServiceCharges, double deliveryFee) {
    double userProportion = userTotalBefore / totalBefore;
    return Math.round((userTotalBefore - (totalDiscounts * userProportion) + (totalServiceCharges * userProportion)
        + (deliveryFee * userProportion)) * 100) / 100.0;
  }

  private static double calculateTotalDiscounts(OrderDto orderDto, double totalBefore) {
    return orderDto.getDiscount()
        + (totalBefore * ((orderDto.getDiscountPercentage() != null ? orderDto.getDiscountPercentage() : 0) / 100));
  }

  private static double calculateTotalServiceCharges(OrderDto orderDto, double totalBefore) {
    return totalBefore
        * ((orderDto.getServiceChargePercentage() != null ? orderDto.getServiceChargePercentage() : 0) / 100);
  }

  private static double calculateTotalAfterDiscountsAndCharges(OrderDto orderDto, double totalBefore,
      double totalDiscounts, double totalServiceCharges) {
    return totalBefore - totalDiscounts + totalServiceCharges + orderDto.getDeliveryFee();
  }

}