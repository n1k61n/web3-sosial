@echo off
REM W3Social - Clean and Restart

echo 🧹 Cleaning up old containers...

docker-compose down
docker-compose rm -f

echo.
echo ✅ Cleanup complete. Now run start.bat

pause
