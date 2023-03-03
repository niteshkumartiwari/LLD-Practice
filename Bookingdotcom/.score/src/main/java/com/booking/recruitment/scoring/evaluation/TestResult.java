package com.booking.recruitment.scoring.evaluation;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TestResult {
  @Getter private final Map<String, Status> submission;

  private final HackerrankYaml hackerrankYaml;
  private final XmlMapper xmlMapper;

  public TestResult(HackerrankYaml hackerrankYaml) {
    this.hackerrankYaml = hackerrankYaml;
    this.xmlMapper =
        XmlMapper.builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build();
    this.submission = Collections.unmodifiableMap(loadSubmission());
  }

  private Map<String, Status> loadSubmission() {
    HashMap<String, Status> submission = new HashMap<>();
    for (String fileName : hackerrankYaml.getScoringFiles()) {
      try {
        String evaluationFileName =
            fileName.replaceAll("^[.]hg", System.getProperty("scoring.dir"));
        MavenSurefireTestResult testResult = xmlToTestResult(evaluationFileName);
        for (MavenSurefireSingleTestResult testCase : testResult.getTestCases()) {
          submission.put(testCase.getName(), testCase.getStatus());
        }
      } catch (IOException e) {
        log.error("Fatal: Unable to load test results", e);
        System.exit(1);
      }
    }
    return submission;
  }

  private MavenSurefireTestResult xmlToTestResult(String fileName) throws IOException {
    try {
      return xmlMapper.readValue(new File(fileName), MavenSurefireTestResult.class);
    } catch (JsonParseException e) {
      log.error(String.format("Fatal: cannot read from test report file %s", fileName), e);
      System.exit(1);
      return null;
    }
  }
}
