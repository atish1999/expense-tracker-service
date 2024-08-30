package com.personal.tracker_service;

import com.personal.tracker_service.model.schema.Transaction;
import com.personal.tracker_service.service.FileParserService;
import java.io.FileInputStream;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TrackerServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(TrackerServiceApplication.class, args);
  }

  @Bean
  CommandLineRunner read(FileParserService fileReaderService) {
    return args -> {
      List<Transaction> transactions =
          fileReaderService.parseFile(
              new FileInputStream(
                  "/Users/atishnaskar/Desktop/Takeout/Google Pay/My Activity/My Activity.html"));
      transactions.forEach(System.out::println);
    };
  }
}
