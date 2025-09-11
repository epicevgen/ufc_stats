# Multi-stage build for UFC Stats application

# Build stage
FROM gradle:8.5-jdk17 AS build

WORKDIR /app

# Copy gradle files
COPY build.gradle settings.gradle ./
COPY gradle/ gradle/

# Download dependencies
RUN gradle dependencies --no-daemon

# Copy source code
COPY src/ src/

# Build application
RUN gradle build --no-daemon -x test

# Runtime stage
FROM eclipse-temurin:17-jre

WORKDIR /app

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Create non-root user
RUN groupadd -r ufcstats && useradd -r -g ufcstats ufcstats

# Copy built application
COPY --from=build /app/build/libs/*.jar app.jar

# Create data directory
RUN mkdir -p /app/data && chown -R ufcstats:ufcstats /app

# Switch to non-root user
USER ufcstats

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
