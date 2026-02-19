#!/bin/bash

# W3Social - Stop All Services

echo "🛑 Stopping all W3Social services..."

docker-compose down

echo "✅ All services stopped."
echo ""
echo "To start again, run: ./start.sh (Linux/Mac) or start.bat (Windows)"
