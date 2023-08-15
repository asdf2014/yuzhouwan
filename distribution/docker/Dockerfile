FROM maven:3.8.5-openjdk-17 as builder

COPY . /src
WORKDIR /src

RUN --mount=type=cache,target=/root/.m2 \
      mvn clean install \
      de.qaware.maven:go-offline-maven-plugin:resolve-dependencies \
      -T 1C \
      -B \
      -ff \
      -DskipTests \
      -Dcheckstyle.skip
