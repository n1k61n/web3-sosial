@echo off
REM Manual deployment script for smart contracts

echo ⛓️  Deploying smart contracts to Ganache...

cd blockchain

REM Check if Ganache is running
echo Checking Ganache connection...
curl -s -X POST -H "Content-Type: application/json" --data "{\"jsonrpc\":\"2.0\",\"method\":\"eth_blockNumber\",\"params\":[],\"id\":1}" http://localhost:8545 >nul 2>&1
if errorlevel 1 (
    echo ❌ Ganache is not running!
    echo Please start services first: start.bat
    echo Or check: docker-compose ps
    exit /b 1
)

echo ✅ Ganache is running

REM Create .env file for deployment with standard test mnemonic
echo GANACHE_MNEMONIC="test test test test test test test test test test test junk" > .env
echo GANACHE_URL=http://localhost:8545 >> .env

REM Run deployment
echo.
echo 🚀 Deploying contracts...
call npm run deploy:local

echo.
echo ✅ Deployment complete!
echo Check blockchain/deployments/localhost.json for contract addresses

pause
