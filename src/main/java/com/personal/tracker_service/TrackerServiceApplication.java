package com.personal.tracker_service;

import com.personal.tracker_service.model.schema.Transaction;
import com.personal.tracker_service.service.FileParserService;
import java.io.FileInputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TrackerServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(TrackerServiceApplication.class, args);
  }

  @Value("${activity.file.location}")
  private String fileLocation;

  @Bean
  CommandLineRunner read(FileParserService fileReaderService) {
    return args -> {
      List<Transaction> transactions =
          fileReaderService.parseFile(new FileInputStream(fileLocation));
      transactions.forEach(System.out::println);
    };
  }
}
