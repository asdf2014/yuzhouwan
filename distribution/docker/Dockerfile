FROM maven:3.8.6-jdk-11-slim as builder

COPY . /src
WORKDIR /src

RUN --mount=type=cache,target=/root/.m2 \
      mvn -B -ff de.qaware.maven:go-offline-maven-plugin:resolve-dependencies \
      clean install \
      -T 1C \
      -DskipTests \
      -Dcheckstyle.skip
