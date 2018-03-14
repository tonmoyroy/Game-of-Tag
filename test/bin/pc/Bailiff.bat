@ECHO OFF
rem -- ----------------------------------------------------------------
rem -- This file is for Ms Windows.
rem -- This file starts a Bailiff from its remote installation point.
rem -- *** IMPORTANT ***
rem -- You must modify the line   set HTTP=...  to point to *your* codebase.
rem -- ----------------------------------------------------------------
set CBO=-Djava.rmi.server.useCodebaseOnly=false

set HTTP=http://www.dsv.su.se/~fk/pislab2/

set CFG=%~dp0.\HTTPD_CFG.BAT

IF NOT EXIST "%CFG%" GOTO nofile
CALL "%CFG%"
SET HTTP=%CODEBASE%

:nofile

set ROOT="%~dp0..\.."

set LIB=%ROOT%/lib

set PCY=%LIB%/policy.all

set JRN=%LIB%/JarRunner.jar

set CBS=%HTTP%/Bailiff-dl.jar

set JAR=%HTTP%/Bailiff.jar

set CLASSPATH=

java %CBO% -Djava.security.policy=%PCY% -Djava.rmi.server.codebase="%CBS%" -jar %JRN% %JAR% %*
