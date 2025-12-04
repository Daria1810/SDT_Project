@echo off
REM StreamFlix Microservices Build Script
REM Builds all 5 microservices with Maven

echo ===================================================
echo   StreamFlix Microservices Build Script
echo   Building all services...
echo ===================================================
echo.

set SERVICES=user-service content-service video-service recommendation-service api-gateway
set FAILED_SERVICES=

for %%s in (%SERVICES%) do (
    echo ---------------------------------------------------
    echo Building %%s...
    echo ---------------------------------------------------
    
    if exist "%%s" (
        cd %%s
        
        call mvn clean package -DskipTests
        if errorlevel 1 (
            echo ERROR: Failed to build %%s
            set FAILED_SERVICES=!FAILED_SERVICES! %%s
        ) else (
            echo SUCCESS: %%s built successfully!
        )
        
        cd ..
    ) else (
        echo WARNING: Directory %%s not found!
        set FAILED_SERVICES=!FAILED_SERVICES! %%s
    )
    
    echo.
)

echo ===================================================
echo   Build Summary
echo ===================================================

if "%FAILED_SERVICES%"=="" (
    echo All services built successfully!
    echo.
    echo Next steps:
    echo 1. Run: docker-compose up -d
    echo 2. Wait 30-60 seconds for services to start
    echo 3. Check status: docker-compose ps
    echo 4. View logs: docker-compose logs -f
    echo 5. Test with Postman collection
    echo.
) else (
    echo Failed to build the following services:
    echo %FAILED_SERVICES%
    echo.
    echo Please fix the errors and run the script again.
    exit /b 1
)

echo ===================================================
pause
