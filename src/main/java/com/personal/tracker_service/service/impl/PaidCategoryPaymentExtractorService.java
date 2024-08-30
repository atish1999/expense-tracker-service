package com.personal.tracker_service.service.impl;

import static com.personal.tracker_service.constant.DateTimeFormat.G_PAY_DATE_TIME_FORMAT;

import com.personal.tracker_service.model.schema.Transaction;
import com.personal.tracker_service.service.PaymentExtractorService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("PAID_PAYMENT_EXTRACTOR")
public class PaidCategoryPaymentExtractorService implements PaymentExtractorService {

  private static final String REGEX =
      "Paid ₹(\\d+\\.\\d{2})(?: to ([\\w\\s]+))?(?: using Bank Account ([A-Z0-9]+))?";

  private static final Pattern PATTERN = Pattern.compile(REGEX);

  @Override
  public Transaction extractPayment(final String paymentDescription, final String paymentDate) {

    if (!paymentDescription.toLowerCase().contains("paid")) {
      throw new IllegalArgumentException(
          "Expected payment category is Paid but it does not contain Paid");
    }

    Matcher matcher = PATTERN.matcher(paymentDescription);

    if (!matcher.find()) {
      throw new IllegalArgumentException(
          "Paid payments description is not matching with the configured matcher for the paymentDescription: %s and paymentDate: %s"
              .formatted(paymentDescription, paymentDate));
    }

    // amount
    String amount = matcher.group(1);
    String receiver = matcher.group(2);
    String account = matcher.group(3);

    // date
    DateTimeFormatter ddMmmYyyy = DateTimeFormatter.ofPattern(G_PAY_DATE_TIME_FORMAT.format());
    LocalDateTime timeStamp = LocalDateTime.parse(paymentDate, ddMmmYyyy);

    return Transaction.builder()
        .amount(BigDecimal.valueOf(Double.parseDouble(amount)))
        .receiver(receiver)
        .timeStamp(timeStamp)
        .sender(account)
        .build();
  }
}
