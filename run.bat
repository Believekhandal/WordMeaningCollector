@echo off
echo Starting Vocabulary Extractor...

echo Setting Java 21+ environment...
set JAVA_HOME=C:\Program Files\Java\jdk-23

echo Starting Backend (Spring Boot)...
start "Backend" cmd /c "mvn spring-boot:run"

echo Waiting for backend to start...
timeout /t 15 /nobreak > nul

echo Starting Frontend (React)...
cd frontend
start "Frontend" cmd /c "npm run dev"

echo.
echo Vocabulary Extractor is starting up!
echo.
echo Backend: http://localhost:9999
echo Frontend: http://localhost:5173
echo.
echo Press any key to stop all services...
pause > nul

echo Stopping services...
taskkill /FI "WINDOWTITLE eq Backend*" /T /F > nul 2>&1
taskkill /FI "WINDOWTITLE eq Frontend*" /T /F > nul 2>&1

echo All services stopped.
pause
