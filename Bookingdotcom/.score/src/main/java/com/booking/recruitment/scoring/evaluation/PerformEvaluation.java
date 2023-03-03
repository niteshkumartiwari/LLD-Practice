package com.booking.recruitment.scoring.evaluation;

public class PerformEvaluation {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    HackerrankYaml hackerrankYaml = new HackerrankYaml();
    TestResult testResult = new TestResult(hackerrankYaml);
    TestEvaluator testEvaluator = new TestEvaluator(hackerrankYaml, testResult);

    double score = testEvaluator.evaluate();
    System.out.println(String.format("Candidate scored: %.2f", score));
  }
}
