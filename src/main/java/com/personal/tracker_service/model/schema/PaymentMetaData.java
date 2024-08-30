package com.personal.tracker_service.model.schema;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class PaymentMetaData {

  private String paymentDescription;
  private String paymentDate;
}
