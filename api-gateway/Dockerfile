FROM amazoncorretto:17-alpine

# Install curl for health checks
RUN apk add --no-cache curl

WORKDIR /app

# Copy the JAR file from target directory
COPY target/api-gateway-1.0.0-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
