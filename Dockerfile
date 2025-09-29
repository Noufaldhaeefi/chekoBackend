# Multi-stage build for production-ready Docker image
# Stage 1: Build the application
FROM maven:3.9.6-openjdk-17-slim AS builder

# Set working directory
WORKDIR /app

# Copy Maven files first for better layer caching
COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application (skip tests in Docker build)
RUN mvn clean package -DskipTests -B

# Stage 2: Create runtime image
FROM openjdk:17-jre-slim AS runtime

# Install necessary packages and create user for security
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd -r cheko && useradd -r -g cheko cheko

# Set working directory
WORKDIR /app

# Copy the JAR file from builder stage
COPY --from=builder /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

# Change ownership to cheko user
RUN chown cheko:cheko app.jar

# Switch to non-root user
USER cheko

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/health || exit 1

# Set JVM options for production
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]