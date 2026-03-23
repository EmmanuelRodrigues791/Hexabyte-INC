@echo off
echo ===========================
echo CartPilot Tester
echo ===========================

set JAVA=C:\Users\manny\.jdks\openjdk-25.0.2\bin
set LIB=lib\mysql-connector-j-9.6.0.jar
set STANDALONE=lib\junit-platform-console-standalone-1.10.4.jar
set AGENT=lib\byte-buddy-agent-1.14.19.jar
set TEST_LIBS=lib\junit-jupiter-api-5.10.0.jar;lib\junit-jupiter-engine-5.10.0.jar;lib\mockito-core-5.12.0.jar;lib\byte-buddy-1.14.19.jar;lib\byte-buddy-agent-1.14.19.jar;lib\objenesis-3.3.jar;lib\junit-platform-commons-1.10.0.jar;lib\junit-platform-engine-1.10.0.jar;lib\opentest4j-1.3.0.jar;lib\apiguardian-api-1.1.2.jar
set OUT=out
set TEST_OUT=out\test

if not exist %TEST_OUT% mkdir %TEST_OUT%

echo [1/3] Compiling tests...
%JAVA%\javac -cp "%OUT%;%LIB%;%TEST_LIBS%" -d %TEST_OUT% test\ItemTest.java test\InventorySystemTest.java

if %ERRORLEVEL% NEQ 0 (
    echo TEST COMPILATION FAILED
    exit /b 1
)

echo [2/3] Tests compiled!
echo [3/3] Running tests...
echo ===========================
%JAVA%\java -Dnet.bytebuddy.experimental=true -javaagent:%AGENT% -cp "%STANDALONE%;%OUT%;%TEST_OUT%;%LIB%;%TEST_LIBS%" org.junit.platform.console.ConsoleLauncher --select-class=ItemTest --select-class=InventorySystemTest