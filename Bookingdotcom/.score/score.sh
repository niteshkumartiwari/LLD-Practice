#!/bin/bash
scoringDir=$(test -d .score && echo .score || echo scoring)

if [ "$1" == "--install-java" ]; then
  javaVersion=15
  filename="zulu15.28.51-ca-jdk15.0.1-linux_amd64.deb"
  wget https://cdn.azul.com/zulu/bin/$filename
  sudo dpkg -i $filename
  export JAVA_HOME=/usr/lib/jvm/zulu-$javaVersion-amd64
  export PATH=/$JAVA_HOME/bin:$PATH
fi

mvn clean test spring-boot:start exec:exec spring-boot:stop -Dmaven.test.failure.ignore=true -Dscoring.dir="${scoringDir}" -Dserver.port=8001 -Dspring-boot.run.arguments="--server.port=\${server.port}"

mvn -q -f "${scoringDir}/pom.xml" compile exec:java -Dscoring.dir="${scoringDir}" -Dexec.mainClass="com.booking.recruitment.scoring.evaluation.PerformEvaluation" | tee scoring.out

exit 0
