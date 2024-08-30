package com.personal.tracker_service.model.schema;

import com.github.f4b6a3.ulid.Ulid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document("transaction")
public class Transaction {

  @Id @Builder.Default private String id = Ulid.fast().toString();
  private BigDecimal amount;
  private String receiver;
  private String sender;
  private LocalDateTime timeStamp;
  private String transactionType;
  private String mode;

  @Override
  public String toString() {
    return amount + " from " + sender + " -> " + receiver;
  }
}
