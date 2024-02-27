package com.example.splitbilleats.domain;

import com.example.splitbilleats.application.dto.OrderDto;
import com.example.splitbilleats.application.dto.OrderItemDto;
import com.example.splitbilleats.application.dto.ParticipantPayment;
import com.example.splitbilleats.application.dto.PaymentSplitDto;
import com.example.splitbilleats.application.dto.UserDto;
import com.example.splitbilleats.domain.service.OrderService;
import com.example.splitbilleats.infrastructure.payment.interfaces.PaymentGateway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

public class OrderServiceTest {

  @Mock
  private PaymentGateway paymentGateway;

  @InjectMocks
  private OrderService orderService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void shouldCalculateSplitTest() {
    OrderItemDto sanduiche = new OrderItemDto(null, "sanduiche", 8.0, 1);
    UserDto amigo1 = new UserDto("Amigo 1", Collections.singletonList(sanduiche));

    OrderItemDto hamburguer = new OrderItemDto(null, "hamburguer", 40.0, 1);
    OrderItemDto sobremesa = new OrderItemDto(null, "sobremesa", 2.0, 1);
    UserDto amigo2 = new UserDto("Amigo 2", Arrays.asList(hamburguer, sobremesa));

    OrderDto orderDto = new OrderDto(null, Arrays.asList(amigo1, amigo2), 20.0, 8.0, 0.0, 0.0);

    when(paymentGateway.generatePaymentLink(31.92)).thenReturn("https://payment.link");
    when(paymentGateway.generatePaymentLink(6.08)).thenReturn("https://payment.link");

    PaymentSplitDto result = orderService.calculateSplit(orderDto);

    assertEquals(2, result.getParticipantPayments().size());

    ParticipantPayment paymentAmigo1 = result.getParticipantPayments().get(0);
    assertEquals("Amigo 1", paymentAmigo1.getName());
    assertTrue(paymentAmigo1.getItems().contains("sanduiche"));
    assertEquals("https://payment.link", paymentAmigo1.getPaymentLink());

    ParticipantPayment paymentAmigo2 = result.getParticipantPayments().get(1);
    assertEquals("Amigo 2", paymentAmigo2.getName());
    assertTrue(paymentAmigo2.getItems().containsAll(Arrays.asList("hamburguer", "sobremesa")));
    assertEquals("https://payment.link", paymentAmigo2.getPaymentLink());

    assertEquals(6.08, paymentAmigo1.getAmountDue(), 0.01);
    assertEquals(31.92, paymentAmigo2.getAmountDue(), 0.01);
  }

  @Test
  void shouldCorrectlyApplyPercentageDiscountsAndServiceCharges() {
    OrderItemDto item = new OrderItemDto(null, "item", 100.0, 1);
    UserDto user = new UserDto("User", Collections.singletonList(item));

    OrderDto orderDto = new OrderDto(null, Collections.singletonList(user), 0.0, 0.0, 10.0, 5.0);

    when(paymentGateway.generatePaymentLink(anyDouble())).thenReturn("https://payment.link");

    PaymentSplitDto result = orderService.calculateSplit(orderDto);

    assertEquals(1, result.getParticipantPayments().size());

    ParticipantPayment payment = result.getParticipantPayments().get(0);
    assertEquals("User", payment.getName());

    double expectedAmountDue = 95;
    assertEquals(expectedAmountDue, payment.getAmountDue(), 0.01);
  }

}