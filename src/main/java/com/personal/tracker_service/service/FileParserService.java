package com.personal.tracker_service.service;

import com.personal.tracker_service.model.schema.Transaction;
import java.io.InputStream;
import java.util.List;

public interface FileParserService {

  List<Transaction> parseFile(final InputStream inputStream);
}
