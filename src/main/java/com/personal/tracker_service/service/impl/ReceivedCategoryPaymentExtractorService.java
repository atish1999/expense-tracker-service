package com.personal.tracker_service.service.impl;

import com.personal.tracker_service.model.schema.Transaction;
import com.personal.tracker_service.service.PaymentExtractorService;
import org.springframework.stereotype.Service;

@Service("RECEIVED_PAYMENT_EXTRACTOR")
public class ReceivedCategoryPaymentExtractorService implements PaymentExtractorService {
  @Override
  public Transaction extractPayment(String paymentDescription, String paymentDate) {
    return null;
  }
}
