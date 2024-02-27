package com.example.splitbilleats.application.dto;

import java.util.List;

public class PaymentSplitDto {
  private Double totalBeforeDiscountsAndFees;
  private Double totalOrderAmount;
  private List<ParticipantPayment> participantPayments;

  public PaymentSplitDto() {

  }

  public PaymentSplitDto(Double totalBeforeDiscountsAndFees, Double totalOrderAmount,
      List<ParticipantPayment> participantPayments) {
    this.totalBeforeDiscountsAndFees = totalBeforeDiscountsAndFees;
    this.totalOrderAmount = totalOrderAmount;
    this.participantPayments = participantPayments;
  }

  public Double getTotalBeforeDiscountsAndFees() {
    return totalBeforeDiscountsAndFees;
  }

  public void setTotalBeforeDiscountsAndFees(Double totalBeforeDiscountsAndFees) {
    this.totalBeforeDiscountsAndFees = totalBeforeDiscountsAndFees;
  }

  public Double getTotalOrderAmount() {
    return totalOrderAmount;
  }

  public void setTotalOrderAmount(Double totalOrderAmount) {
    this.totalOrderAmount = totalOrderAmount;
  }

  public List<ParticipantPayment> getParticipantPayments() {
    return participantPayments;
  }

  public void setParticipantPayments(List<ParticipantPayment> participantPayments) {
    this.participantPayments = participantPayments;
  }

}
