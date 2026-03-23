@echo off
echo ===========================
echo CartPilot Build Script
echo ===========================

set JAVA=C:\Users\manny\.jdks\openjdk-25.0.2\bin
set LIB=lib\mysql-connector-j-9.6.0.jar
set SRC=src\Main.java src\InventorySystem.java src\Item.java src\LoginPage.java src\ManagerPage.java src\EmployeePage.java
set OUT=out

if not exist %OUT% mkdir %OUT%

echo [1/3] Compiling source files...
%JAVA%\javac -cp "%LIB%" -d %OUT% %SRC%

if %ERRORLEVEL% NEQ 0 (
    echo BUILD FAILED
    exit /b 1
)

echo [2/3] Compilation successful!
echo [3/3] Launching CartPilot...
echo ===========================
%JAVA%\java -cp "%OUT%;%LIB%" Main