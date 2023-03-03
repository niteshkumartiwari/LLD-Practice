package com.booking.recruitment.scoring.evaluation;

import lombok.Data;

@Data
public class MavenSurefireSingleTestResult {
  @Data
  static class Failure {
    String message;
  }

  @Data
  static class Error {
    String message;
  }

  private String name;
  private Failure failure;
  private Error error;

  public Status getStatus() {
    if (failure == null && error == null) {
      return Status.PASSED;
    }
    return Status.FAILED;
  }
}
