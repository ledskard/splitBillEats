package com.example.splitbilleats.infrastructure.payment.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.splitbilleats.infrastructure.payment.interfaces.PaymentGateway;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class StripePaymentGateway implements PaymentGateway {
  public StripePaymentGateway(@Value("${stripe.api.key}") String stripeApiKey) {
    Stripe.apiKey = stripeApiKey;
  }

  public String generatePaymentLink(double amountDue) {
    long amountInCents = Math.round(amountDue * 100);

    SessionCreateParams params = SessionCreateParams.builder()
        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
        .setMode(SessionCreateParams.Mode.PAYMENT)
        .setSuccessUrl("https://example.com/success")
        .setCancelUrl("https://example.com/cancel")
        .addLineItem(SessionCreateParams.LineItem.builder()
            .setQuantity(1L)
            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("brl")
                .setUnitAmount(amountInCents)
                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                    .setName("Lanche Compartilhado")
                    .build())
                .build())
            .build())
        .build();

    try {
      Session session = Session.create(params);
      return session.getUrl();
    } catch (StripeException e) {
      e.printStackTrace();
      return null;
    }
  }
}