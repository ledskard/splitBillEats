package com.example.splitbilleats.infrastructure.payment.interfaces;

import org.springframework.stereotype.Component;

@Component
public interface PaymentGateway {
  String generatePaymentLink(double amountDue);
}