#!/bin/bash
scoringOutputFile="$1"
hackerrankYamlFile="$2"

# shellcheck disable=SC2086
test "$(tail -n1 $scoringOutputFile | grep -Eo '[0-9.]+$')" = "$(grep 'test_total_score:' $hackerrankYamlFile | grep -Eo '[0-9.]+').00"
