package com.booking.recruitment.scoring.reports;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class TestCoverageReport {
  private final Integer run;
  private final Integer failed;
  private final Integer errored;

  public TestCoverageReport() {
    run = null;
    failed = null;
    errored = null;
  }

  public TestCoverageReport(String run, String failed, String errored) {
    this.run = fromString(run);
    this.failed = fromString(failed);
    this.errored = fromString(errored);
  }

  private static Integer fromString(String s) {
    Integer result = null;
    if (s != null) {
      try {
        result = Integer.valueOf(s);
      } catch (NumberFormatException ignored) {
      }
    }
    return result;
  }

  public TestCoverageReport combine(TestCoverageReport other) {
    int run = 0;
    int failed = 0;
    int errored = 0;

    if (other != null) {
      if (other.getRun() != null) {
        run = other.getRun();
      }
      if (other.getFailed() != null) {
        failed = other.getFailed();
      }
      if (other.getErrored() != null) {
        errored = other.getErrored();
      }
    }

    if (this.getRun() != null) {
      run += this.getRun();
    }
    if (this.getFailed() != null) {
      failed += this.getFailed();
    }
    if (this.getErrored() != null) {
      errored += this.getErrored();
    }

    return new TestCoverageReport(run, failed, errored);
  }
}
