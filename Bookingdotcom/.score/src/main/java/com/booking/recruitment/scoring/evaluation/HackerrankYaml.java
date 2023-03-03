package com.booking.recruitment.scoring.evaluation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Slf4j
@SuppressWarnings("unchecked")
public class HackerrankYaml {
  private final Map<String, Double> testWeights;
  private final List<String> scoringFiles;
  private final Integer maxScore;

  public HackerrankYaml() {
    ObjectMapper om = new ObjectMapper(new YAMLFactory());

    InputStream input = this.getClass().getResourceAsStream("/hackerrank.yml");

    Map<String, ?> map = null;
    try {
      map = om.readValue(input, Map.class);
    } catch (IOException e) {
      log.error("Fatal: unable to locale hackerrank.yaml file");
    }

    testWeights = getTestWeightsFromYaml(map);
    maxScore = getMaxScoreFromYaml(map);
    scoringFiles = getScoringFilesFromYaml(map);
  }

  private Map<String, Double> getTestWeightsFromYaml(Map<String, ?> map) {
    if (map != null) {
      try {
        return Objects.requireNonNull(
            Collections.unmodifiableMap(
                ((Map<String, Map<String, Map<String, Double>>>) map.get("configuration"))
                    .get("scoring")
                    .get("testcase_weights")));
      } catch (NullPointerException e) {
        log.error("Fatal: unable to read test case weights", e);
        System.exit(1);
      }
    }
    return Collections.emptyMap();
  }

  private Integer getMaxScoreFromYaml(Map<String, ?> map) {
    Integer result = null;
    if (map != null) {
      try {
        result =
            ((Map<String, Map<String, Integer>>) map.get("configuration"))
                .get("scoring")
                .get("test_total_score");
      } catch (Exception e) {
        log.error("Fatal: unable to read test max score", e);
        System.exit(1);
      }
    }
    return result;
  }

  private List<String> getScoringFilesFromYaml(Map<String, ?> map) {
    if (map != null) {
      try {
        return ((Map<String, Map<String, List<String>>>) map.get("configuration"))
            .get("scoring")
            .get("files");
      } catch (Exception e) {
        log.error("Fatal: unable to read test max score", e);
        System.exit(1);
      }
    }
    return Collections.emptyList();
  }
}
