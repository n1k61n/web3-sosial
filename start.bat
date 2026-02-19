@echo off
REM W3Social - Full Stack Startup Script for Windows
REM Simplified version - deployment can be done separately

echo 🚀 W3Social - Starting Full Stack...

REM Check if Docker is running
docker info >nul 2>&1
if errorlevel 1 (
    echo ❌ Docker is not running. Please start Docker Desktop first.
    exit /b 1
)

echo ✅ Docker is running

REM Step 1: Build blockchain contracts (optional, for local testing)
echo.
echo 📦 Step 1/3: Checking blockchain contracts...
cd blockchain
if not exist "node_modules" (
    echo Installing npm dependencies...
    call npm install
)
if not exist "artifacts" (
    echo Compiling contracts...
    call npm run compile
)
cd ..

REM Step 2: Build all Docker images
echo.
echo 🐳 Step 2/3: Building Docker images...
docker-compose build

REM Step 3: Start all services
echo.
echo ▶️  Step 3/3: Starting all services...
docker-compose up -d

REM Wait for services to be ready
echo.
echo ⏳ Waiting for services to start (this may take 2-3 minutes)...
echo.

REM Check services one by one
echo Checking Ganache...
timeout /t 10 /nobreak >nul

echo Checking Eureka Server...
timeout /t 20 /nobreak >nul

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
echo ⛓️  OPTIONAL: Deploy smart contracts
echo ════════════════════════════════════════
echo Run: deploy-contracts.bat
echo Or:  docker-compose run --rm ganache-deploy
echo.

echo 📝 Useful commands:
echo   docker-compose logs -f     ^| View all logs
echo   docker-compose logs [svc]  ^| View specific service logs
echo   docker-compose down        ^| Stop all services
echo   docker-compose ps          ^| Check service status
echo.

pause
