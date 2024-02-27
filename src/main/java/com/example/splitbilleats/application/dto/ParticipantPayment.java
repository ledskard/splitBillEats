package com.example.splitbilleats.application.dto;

import java.util.List;

public class ParticipantPayment {
  private String name;
  private Double amountDue;
  private List<String> items;
  private String paymentLink;

  public ParticipantPayment(String name, Double amountDue, List<String> items, String paymentLink) {
    this.name = name;
    this.amountDue = amountDue;
    this.items = items;
    this.paymentLink = paymentLink;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getAmountDue() {
    return amountDue;
  }

  public void setAmountDue(Double amountDue) {
    this.amountDue = amountDue;
  }

  public List<String> getItems() {
    return items;
  }

  public void setItems(List<String> items) {
    this.items = items;
  }

  public String getPaymentLink() {
    return paymentLink;
  }

  public void setPaymentLink(String paymentLink) {
    this.paymentLink = paymentLink;
  }
}