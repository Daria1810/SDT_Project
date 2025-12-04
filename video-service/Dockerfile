FROM amazoncorretto:17-alpine

# Install curl for health checks
RUN apk add --no-cache curl

WORKDIR /app

# Copy the JAR file from target directory
COPY target/video-service-1.0.0-SNAPSHOT.jar app.jar

# Expose port 8083
EXPOSE 8083

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8083/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
