# Stage 1: Build
FROM quay.io/quarkus/ubi9-quarkus-mandrel-builder-image:jdk-25 AS build
COPY --chown=quarkus:quarkus --chmod=0755 mvnw /code/mvnw
COPY --chown=quarkus:quarkus .mvn /code/.mvn
COPY --chown=quarkus:quarkus pom.xml /code/

USER quarkus
WORKDIR /code

RUN ./mvnw -B org.apache.maven.plugins:maven-dependency-plugin:3.8.1:go-offline

COPY --chown=quarkus:quarkus src /code/src
RUN ./mvnw package -Dnative -DskipTests

# Stage 2: Runtime
FROM quay.io/quarkus/ubi9-quarkus-micro-image:2.0

WORKDIR /work/

COPY --from=build --chown=1001:root --chmod=755 /code/target/*-runner /work/application

EXPOSE 8080
USER 1001

ENTRYPOINT ["./application", "-Dquarkus.http.host=0.0.0.0"]