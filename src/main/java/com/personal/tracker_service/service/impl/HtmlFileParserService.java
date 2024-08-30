package com.personal.tracker_service.service.impl;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.personal.tracker_service.model.schema.PaymentMetaData;
import com.personal.tracker_service.model.schema.Transaction;
import com.personal.tracker_service.service.FileParserService;
import com.personal.tracker_service.service.PaymentExtractorService;
import java.io.InputStream;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("HTML")
@Slf4j
@RequiredArgsConstructor
public class HtmlFileParserService implements FileParserService {

  private final Map<String, PaymentExtractorService> paymentExtractorServiceMap;

  @Value("${transaction.meta.data.year:2023}")
  private String prohibitedYear;

  private String prohibitedYear() {
    return prohibitedYear;
  }

  @Override
  public List<Transaction> parseFile(InputStream inputStream) {

    List<Transaction> transactions = new ArrayList<>();

    try {
      final Document parsedDocument = Jsoup.parse(inputStream, "UTF-8", "");
      log.info("Title: {}", parsedDocument.title());

      final Elements parentElements = parsedDocument.select("div.mdl-grid");

      log.info("No of elements: {}", parentElements.size());

      for (Element parentElement : parentElements) {

        Element paymentDescriptionElement =
            parentElement
                .select(
                    ".content-cell.mdl-cell--6-col.mdl-typography--body-1:not(.mdl-typography--text-right)")
                .first();

        if (paymentDescriptionElement == null) {
          continue;
        }

        final PaymentMetaData paymentMetaData =
            PaymentExtractorService.extractPaymentMetaData(paymentDescriptionElement);

        if (Objects.nonNull(paymentMetaData)
            && paymentMetaData.getPaymentDate().contains(prohibitedYear())) {
          break;
        }

        Optional.ofNullable(getTransactionFromTransactionMetaData(paymentMetaData))
            .ifPresent(transactions::add);
      }

    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }

    return transactions;
  }

  private Transaction getTransactionFromTransactionMetaData(final PaymentMetaData paymentMetaData) {

    if (StringUtils.isEmpty(paymentMetaData.getPaymentDescription())) {
      return null;
    }

    String paymentType = "";

    if (paymentMetaData.getPaymentDescription().toLowerCase().contains("paid")) {
      paymentType = "PAID";
    } else if (paymentMetaData.getPaymentDescription().toLowerCase().contains("received")) {
      paymentType = "RECEIVED";
    }

    if (EMPTY.equals(paymentType)) {
      return null;
    }

    String paymentExtractorKey = paymentType + "_PAYMENT_EXTRACTOR";
    final PaymentExtractorService paymentExtractorService =
        paymentExtractorServiceMap.get(paymentExtractorKey);
    return paymentExtractorService.extractPayment(
        paymentMetaData.getPaymentDescription(), paymentMetaData.getPaymentDate());
  }
}
