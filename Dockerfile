# syntax=docker/dockerfile:1.7

FROM eclipse-temurin:26-jdk AS build
WORKDIR /workspace

# Install Maven only in build stage
RUN apt-get update \
    && apt-get install -y --no-install-recommends maven \
    && rm -rf /var/lib/apt/lists/*

# Cache dependencies
COPY pom.xml ./
RUN mvn -B -q -DskipTests dependency:go-offline

# Build application
COPY src ./src
RUN mvn -B -DskipTests clean package

FROM eclipse-temurin:26-jre AS runtime
WORKDIR /app

# Create non-root user
RUN groupadd -r spring && useradd -r -g spring spring

# Copy executable jar from build stage
COPY --from=build /workspace/target/reactive-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
USER spring:spring

ENTRYPOINT ["java", "-XX:+UseZGC", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
