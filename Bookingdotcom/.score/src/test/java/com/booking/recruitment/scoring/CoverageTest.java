package com.booking.recruitment.scoring;

import com.booking.recruitment.scoring.reports.TestCoverageCalculator;
import com.booking.recruitment.scoring.reports.TestCoverageReport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@Slf4j
class CoverageTest {
  private static TestCoverageReport report;

  @BeforeAll
  static void setup() {
    report = TestCoverageCalculator.getStatsOfTestsRun();
  }

  private void testsAreAllPassing() {
    assertThat("Reports failed", report.getFailed(), equalTo(0));
    assertThat("Reports errored", report.getErrored(), equalTo(0));
  }

  @ParameterizedTest
  @ValueSource(ints = {2, 3, 4, 5})
  void wroteAtLeastXNumberOfTests(int number) {
    testsAreAllPassing();
    assertThat("Tests run", report.getRun(), greaterThanOrEqualTo(number));
  }
}
