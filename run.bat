@echo off
setlocal enabledelayedexpansion

set SUBDIR=author-service
set MAIN_DIR=%CD%

cd %SUBDIR%
start /B mvn spring-boot:run
for /f "tokens=2 delims= " %%A in ('wmic process where "name='java.exe'" get ProcessId ^| findstr [0-9]') do set SUBPROJECT_PID=%%A
cd %MAIN_DIR%

start /B mvn spring-boot:run
for /f "tokens=2 delims= " %%A in ('wmic process where "name='java.exe'" get ProcessId ^| findstr [0-9]') do set MAINPROJECT_PID=%%A

pause >nul

if not "%SUBPROJECT_PID%"=="" taskkill /PID %SUBPROJECT_PID% /F
if not "%MAINPROJECT_PID%"=="" taskkill /PID %MAINPROJECT_PID% /F

exit /b
