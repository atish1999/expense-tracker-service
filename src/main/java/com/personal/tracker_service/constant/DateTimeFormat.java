package com.personal.tracker_service.constant;

public enum DateTimeFormat {
  G_PAY_DATE_TIME_FORMAT("d MMM yyyy HH:mm:ss z");

  private final String dateFormat;

  DateTimeFormat(String dateFormat) {
    this.dateFormat = dateFormat;
  }

  public String format() {
    return this.dateFormat;
  }
}
