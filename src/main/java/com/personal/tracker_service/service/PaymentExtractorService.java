package com.personal.tracker_service.service;

import static org.apache.commons.lang3.StringUtils.*;

import com.personal.tracker_service.model.schema.PaymentMetaData;
import com.personal.tracker_service.model.schema.Transaction;
import org.jsoup.nodes.Element;

public interface PaymentExtractorService {

  String PAYMENT_META_DATA_SPLITTER = "<br>";
  String COMMA_DELIMETER = ",";

  Transaction extractPayment(String paymentDescription, String paymentDate);

  static PaymentMetaData extractPaymentMetaData(Element paymentElement) {

    String[] transactionsMetaData = paymentElement.html().trim().split(PAYMENT_META_DATA_SPLITTER);

    if (transactionsMetaData.length < 2) {
      throw new IllegalArgumentException(
          "Payment Description should contain 2 lines separated by <br>, and current payment description is %s"
              .formatted(String.join(SPACE, transactionsMetaData)));
    }

    String paymentDescription =
        transactionsMetaData[0].replace(LF, EMPTY).replace(COMMA_DELIMETER, EMPTY).trim();
    String paymentDate =
        transactionsMetaData[1]
            .replace(LF, EMPTY)
            .replace(COMMA_DELIMETER, EMPTY)
            .replace("Sept", "Sep")
            .trim();
    return PaymentMetaData.builder()
        .paymentDescription(paymentDescription)
        .paymentDate(paymentDate)
        .build();
  }
}
