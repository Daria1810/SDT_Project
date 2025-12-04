#!/bin/bash

# StreamFlix Microservices Build Script
# Builds all 5 microservices with Maven

set -e  # Exit on error

echo "==================================================="
echo "  StreamFlix Microservices Build Script"
echo "  Building all services..."
echo "==================================================="
echo ""

# Define services
services=("user-service" "content-service" "video-service" "recommendation-service" "api-gateway")

# Track build status
failed_services=()

# Build each service
for service in "${services[@]}"; do
    echo "---------------------------------------------------"
    echo "Building $service..."
    echo "---------------------------------------------------"
    
    if [ -d "$service" ]; then
        cd "$service"
        
        if mvn clean package -DskipTests; then
            echo "✅ $service built successfully!"
        else
            echo "❌ ERROR: Failed to build $service"
            failed_services+=("$service")
        fi
        
        cd ..
    else
        echo "⚠️  WARNING: Directory $service not found!"
        failed_services+=("$service")
    fi
    
    echo ""
done

echo "==================================================="
echo "  Build Summary"
echo "==================================================="

if [ ${#failed_services[@]} -eq 0 ]; then
    echo "✅ All services built successfully!"
    echo ""
    echo "Next steps:"
    echo "1. Run: docker-compose up -d"
    echo "2. Wait 30-60 seconds for services to start"
    echo "3. Check status: docker-compose ps"
    echo "4. View logs: docker-compose logs -f"
    echo "5. Test with Postman collection"
    echo ""
else
    echo "❌ Failed to build the following services:"
    for service in "${failed_services[@]}"; do
        echo "   - $service"
    done
    echo ""
    echo "Please fix the errors and run the script again."
    exit 1
fi

echo "==================================================="
