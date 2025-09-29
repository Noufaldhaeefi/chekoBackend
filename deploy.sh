#!/bin/bash

# Cheko Backend Deployment Script
# This script helps deploy the application in different environments

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Default values
ENVIRONMENT="docker"
BUILD_FRESH=false
SKIP_TESTS=false
PULL_LATEST=false

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to show usage
show_usage() {
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  -e, --environment ENV    Set environment (docker, prod) [default: docker]"
    echo "  -b, --build             Build fresh Docker image"
    echo "  -s, --skip-tests        Skip running tests"
    echo "  -p, --pull              Pull latest base images"
    echo "  -h, --help              Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0                      # Deploy with default settings (docker)"
    echo "  $0 -e prod -b           # Deploy to production with fresh build"
    echo "  $0 -e docker -s         # Deploy to docker skipping tests"
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -e|--environment)
            ENVIRONMENT="$2"
            shift 2
            ;;
        -b|--build)
            BUILD_FRESH=true
            shift
            ;;
        -s|--skip-tests)
            SKIP_TESTS=true
            shift
            ;;
        -p|--pull)
            PULL_LATEST=true
            shift
            ;;
        -h|--help)
            show_usage
            exit 0
            ;;
        *)
            print_error "Unknown option: $1"
            show_usage
            exit 1
            ;;
    esac
done

print_status "Starting Cheko Backend deployment..."
print_status "Environment: $ENVIRONMENT"
print_status "Build fresh: $BUILD_FRESH"
print_status "Skip tests: $SKIP_TESTS"
print_status "Pull latest: $PULL_LATEST"

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    print_error "Docker is not running. Please start Docker and try again."
    exit 1
fi

# Check if docker-compose is available
if ! command -v docker-compose &> /dev/null; then
    print_error "docker-compose is not installed. Please install it and try again."
    exit 1
fi

# Create necessary directories
print_status "Creating necessary directories..."
mkdir -p logs
mkdir -p backups

# Set environment variables based on environment
case $ENVIRONMENT in
    "prod")
        export SPRING_PROFILES_ACTIVE=prod
        export SWAGGER_ENABLED=false
        export LOG_LEVEL=WARN
        export JPA_SHOW_SQL=false
        print_warning "Production environment selected. Swagger UI will be disabled."
        ;;
    "docker")
        export SPRING_PROFILES_ACTIVE=docker
        export SWAGGER_ENABLED=true
        export LOG_LEVEL=INFO
        export JPA_SHOW_SQL=false
        ;;
    *)
        print_error "Invalid environment: $ENVIRONMENT. Use 'docker' or 'prod'."
        exit 1
        ;;
esac

# Pull latest images if requested
if [ "$PULL_LATEST" = true ]; then
    print_status "Pulling latest base images..."
    docker-compose pull postgres
fi

# Build and deploy
if [ "$BUILD_FRESH" = true ]; then
    print_status "Building fresh Docker image..."
    if [ "$SKIP_TESTS" = true ]; then
        docker-compose build --no-cache --build-arg SKIP_TESTS=true
    else
        docker-compose build --no-cache
    fi
fi

# Stop existing containers
print_status "Stopping existing containers..."
docker-compose down

# Start services
print_status "Starting services..."
docker-compose up -d

# Wait for services to be healthy
print_status "Waiting for services to be healthy..."
sleep 10

# Check if services are running
if docker-compose ps | grep -q "Up"; then
    print_success "Services started successfully!"
    
    # Display service information
    print_status "Service Information:"
    echo "  - Backend API: http://localhost:${SERVER_PORT:-8080}"
    echo "  - Swagger UI: http://localhost:${SERVER_PORT:-8080}/swagger-ui.html"
    echo "  - Health Check: http://localhost:${SERVER_PORT:-8080}/api/health"
    echo "  - Database: postgresql://localhost:${POSTGRES_PORT:-5432}/${POSTGRES_DB:-cheko}"
    
    # Test health endpoint
    print_status "Testing health endpoint..."
    sleep 5
    if curl -f http://localhost:${SERVER_PORT:-8080}/api/health > /dev/null 2>&1; then
        print_success "Health check passed!"
    else
        print_warning "Health check failed. Check logs with: docker-compose logs cheko-backend"
    fi
else
    print_error "Failed to start services. Check logs with: docker-compose logs"
    exit 1
fi

print_success "Deployment completed successfully!"
print_status "To view logs: docker-compose logs -f"
print_status "To stop services: docker-compose down"
