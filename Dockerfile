FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /workspace

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src src
RUN mvn clean package -DskipTests


FROM eclipse-temurin:25-jre

WORKDIR /app

RUN apt-get update \
    && apt-get install --yes --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && useradd --system --uid 1001 spring

COPY --from=build \
    /workspace/target/stark-bank-webhook-*.jar \
    application.jar

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "application.jar"]
