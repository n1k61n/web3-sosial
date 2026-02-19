@echo off
REM W3Social - Full Stack Startup Script for Windows
REM This script builds and starts all services with Docker

echo 🚀 W3Social - Starting Full Stack...

REM Check if Docker is running
docker info >nul 2>&1
if errorlevel 1 (
    echo ❌ Docker is not running. Please start Docker Desktop first.
    exit /b 1
)

echo ✅ Docker is running

REM Step 1: Build blockchain contracts
echo.
echo 📦 Step 1/4: Building blockchain contracts...
cd blockchain
if not exist "node_modules" (
    echo Installing npm dependencies...
    call npm install
)
call npm run compile
cd ..

REM Step 2: Build all Docker images
echo.
echo 🐳 Step 2/4: Building Docker images...
docker-compose build

REM Step 3: Start all services
echo.
echo ▶️  Step 3/4: Starting all services...
docker-compose up -d

REM Step 4: Wait for services to be ready
echo.
echo ⏳ Step 4/4: Waiting for services to start...
timeout /t 30 /nobreak >nul

REM Check service health
echo.
echo 📊 Service Status:
docker-compose ps

REM Deploy smart contracts
echo.
echo ⛓️  Deploying smart contracts to local Ganache...
cd blockchain
call npm run deploy:local
cd ..

echo.
echo ✅ All services started successfully!

echo.
echo ════════════════════════════════════════
echo 🌐 Service URLs:
echo ════════════════════════════════════════
echo   Frontend:        http://localhost:5173
echo   API Gateway:     http://localhost:8080
echo   Eureka:          http://localhost:8761
echo   Blockchain:      http://localhost:8085
echo   IPFS Gateway:    http://localhost:8081
echo   Ganache RPC:     http://localhost:8545
echo ════════════════════════════════════════

echo.
echo 📝 Useful commands:
echo   docker-compose logs -f     ^| View all logs
echo   docker-compose logs [svc]  ^| View specific service logs
echo   docker-compose down        ^| Stop all services
echo   docker-compose ps          ^| Check service status
echo.

pause
