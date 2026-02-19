#!/bin/bash

# W3Social - Full Stack Startup Script
# This script builds and starts all services with Docker

set -e

echo "🚀 W3Social - Starting Full Stack..."

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}❌ Docker is not running. Please start Docker Desktop first.${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Docker is running${NC}"

# Step 1: Build blockchain contracts
echo -e "\n${YELLOW}📦 Step 1/4: Building blockchain contracts...${NC}"
cd blockchain
if [ ! -d "node_modules" ]; then
    echo "Installing npm dependencies..."
    npm install
fi
npm run compile
cd ..

# Step 2: Build all Docker images
echo -e "\n${YELLOW}🐳 Step 2/4: Building Docker images...${NC}"
docker-compose build

# Step 3: Start all services
echo -e "\n${YELLOW}▶️  Step 3/4: Starting all services...${NC}"
docker-compose up -d

# Step 4: Wait for services to be ready
echo -e "\n${YELLOW}⏳ Step 4/4: Waiting for services to start...${NC}"
sleep 30

# Check service health
echo -e "\n${GREEN}📊 Service Status:${NC}"
docker-compose ps

# Deploy smart contracts
echo -e "\n${YELLOW}⛓️  Deploying smart contracts to local Ganache...${NC}"
cd blockchain
npm run deploy:local
cd ..

echo -e "\n${GREEN}✅ All services started successfully!${NC}"

echo -e "\n${GREEN}════════════════════════════════════════${NC}"
echo -e "${GREEN}🌐 Service URLs:${NC}"
echo -e "${GREEN}════════════════════════════════════════${NC}"
echo -e "  Frontend:        http://localhost:5173"
echo -e "  API Gateway:     http://localhost:8080"
echo -e "  Eureka:          http://localhost:8761"
echo -e "  Blockchain:      http://localhost:8085"
echo -e "  IPFS Gateway:    http://localhost:8081"
echo -e "  Ganache RPC:     http://localhost:8545"
echo -e "${GREEN}════════════════════════════════════════${NC}"

echo -e "\n${YELLOW}📝 Useful commands:${NC}"
echo -e "  docker-compose logs -f     # View all logs"
echo -e "  docker-compose logs [svc]  # View specific service logs"
echo -e "  docker-compose down        # Stop all services"
echo -e "  docker-compose ps          # Check service status"
echo -e ""
