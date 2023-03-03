package com.booking.recruitment.scoring.evaluation;

import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

@AllArgsConstructor
public class TestEvaluator {

  private final HackerrankYaml hackerrankYaml;
  private final TestResult testResult;

  public double evaluate() {
    Map<String, Double> testWeights = hackerrankYaml.getTestWeights();
    double result =
        testResult.getSubmission().entrySet().stream()
            .filter(entry -> Status.PASSED.equals(entry.getValue()))
            .flatMap(entry -> Stream.of(entry.getKey()))
            .flatMapToDouble(testName -> DoubleStream.of(getWeightedScore(testWeights, testName)))
            .reduce(0.0d, Double::sum);

    return result;
  }

  private Double getWeightedScore(Map<String, Double> testWeights, String testName) {
    return testWeights.getOrDefault(testName, 0.0d) * hackerrankYaml.getMaxScore();
  }
}
