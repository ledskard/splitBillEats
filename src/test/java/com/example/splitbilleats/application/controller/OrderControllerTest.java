package com.example.splitbilleats.application.controller;

import com.example.splitbilleats.application.dto.OrderDto;
import com.example.splitbilleats.application.dto.OrderItemDto;
import com.example.splitbilleats.application.dto.ParticipantPayment;
import com.example.splitbilleats.application.dto.PaymentSplitDto;
import com.example.splitbilleats.application.dto.UserDto;
import com.example.splitbilleats.domain.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OrderService orderService;

  private ObjectMapper objectMapper;

  @BeforeEach
  public void setUp() {
    objectMapper = new ObjectMapper();
  }

  @Test
  public void createOrderShouldReturnCorrectResponseWhenOrderIsValid() throws Exception {
    OrderItemDto item1 = new OrderItemDto(1L, "Pizza", 30.0, 1);
    UserDto user1 = new UserDto("Amigo 1", List.of(item1));
    OrderDto orderDto = new OrderDto(null, List.of(user1), 5.0, 2.0, 0.0, 0.0);

    PaymentSplitDto expectedResponse = new PaymentSplitDto(32.0, 32.0,
        List.of(new ParticipantPayment("Amigo 2", 32.0, List.of("Pizza"), "payment.link")));
    given(orderService.calculateSplit(any(OrderDto.class))).willReturn(expectedResponse);

    mockMvc.perform(post("/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(orderDto)))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
  }

  @Test
  public void createOrderShouldReturnBadRequestWhenUserListIsEmpty() throws Exception {
    OrderDto orderDto = new OrderDto(null, Collections.emptyList(), 5.0, 2.0, 0.0, 0.0);

    mockMvc.perform(post("/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(orderDto)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.users").value("At least one user must be provided"));
  }

  @Test
  public void createOrderShouldReturnBadRequestWhenNegativeValuesAreProvided() throws Exception {
    OrderItemDto item1 = new OrderItemDto(1L, "Pizza", -30.0, 1);
    UserDto user1 = new UserDto("Amigo 1", List.of(item1));
    OrderDto orderDto = new OrderDto(null, List.of(user1), -5.0, -2.0, 0.0, 0.0); // Valores negativos

    mockMvc.perform(post("/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(orderDto)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void createOrderShouldProcessCorrectlyWithMultipleUsersAndItems() throws Exception {
    OrderItemDto item1 = new OrderItemDto(1L, "Pizza", 30.0, 2);
    OrderItemDto item2 = new OrderItemDto(2L, "Hamburguer", 5.0, 2);
    UserDto user1 = new UserDto("Amigo 1", List.of(item1));
    UserDto user2 = new UserDto("Amigo 2", List.of(item2));
    OrderDto orderDto = new OrderDto(null, Arrays.asList(user1, user2), 10.0, 5.0, 0.0, 0.0);

    PaymentSplitDto expectedResponse = new PaymentSplitDto(70.0, 70.0,
        Arrays.asList(
            new ParticipantPayment("Amigo 1", 35.0, List.of("Pizza"), "payment.link"),
            new ParticipantPayment("Amigo 2", 35.0, List.of("Hamburguer"), "payment.link")));
    given(orderService.calculateSplit(any(OrderDto.class))).willReturn(expectedResponse);

    mockMvc.perform(post("/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(orderDto)))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
  }

}
