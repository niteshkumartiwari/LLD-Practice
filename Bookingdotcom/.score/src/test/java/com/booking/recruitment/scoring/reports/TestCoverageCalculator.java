package com.booking.recruitment.scoring.reports;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
public final class TestCoverageCalculator {
  private TestCoverageCalculator() {}

  public static TestCoverageReport getStatsOfTestsRun() {
    String existingTests = System.getProperty("existing.tests");
    TestCoverageReport start;
    try {
      start = new TestCoverageReport(Integer.parseInt(existingTests) * -1, null, null);
    } catch (NumberFormatException e) {
      start = new TestCoverageReport();
    }

    return Arrays.stream(Objects.requireNonNull(new File("../target/surefire-reports").listFiles()))
        .filter(file -> file.getName().endsWith(".xml"))
        .map(TestCoverageCalculator::asString)
        .map(TestCoverageCalculator::xmlStringToDocument)
        .filter(Objects::nonNull)
        .map(TestCoverageCalculator::documentToCoverageReport)
        .reduce(start, (accum, element) -> element.combine(accum));
  }

  private static String asString(File file) {
    try {
      return new String(Files.readAllBytes(file.toPath()));
    } catch (IOException e) {
      return "";
    }
  }

  private static Document xmlStringToDocument(String xmlString) {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      return builder.parse(new InputSource(new StringReader(xmlString)));
    } catch (Exception e) {
      log.error("Unable to parse XML result", e);
      return null;
    }
  }

  private static TestCoverageReport documentToCoverageReport(Document document) {
    Node rootNode = document.getFirstChild();
    NamedNodeMap attributes = rootNode.getAttributes();
    String numberOfTests = attributes.getNamedItem("tests").getNodeValue();
    String testsFailed = attributes.getNamedItem("failures").getNodeValue();
    String testsErrored = attributes.getNamedItem("errors").getNodeValue();
    return new TestCoverageReport(numberOfTests, testsFailed, testsErrored);
  }
}
