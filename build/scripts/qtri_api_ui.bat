@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  qtri_api_ui startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and QTRI_API_UI_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\qtri_api_ui-1.0-SNAPSHOT.jar;%APP_HOME%\lib\selenium-java-4.21.0.jar;%APP_HOME%\lib\poi-ooxml-5.3.0.jar;%APP_HOME%\lib\poi-5.3.0.jar;%APP_HOME%\lib\extentreports-2.40.2.jar;%APP_HOME%\lib\rest-assured-5.2.0.jar;%APP_HOME%\lib\json-schema-validator-4.3.3.jar;%APP_HOME%\lib\json-20090211.jar;%APP_HOME%\lib\json-schema-validator-2.2.14.jar;%APP_HOME%\lib\json-schema-core-1.2.14.jar;%APP_HOME%\lib\jackson-coreutils-equivalence-1.0.jar;%APP_HOME%\lib\jackson-coreutils-2.0.jar;%APP_HOME%\lib\jackson-databind-2.18.3.jar;%APP_HOME%\lib\jackson-core-2.18.3.jar;%APP_HOME%\lib\jackson-annotations-2.18.3.jar;%APP_HOME%\lib\jackson-dataformat-xml-2.18.3.jar;%APP_HOME%\lib\selenium-chrome-driver-4.21.0.jar;%APP_HOME%\lib\selenium-devtools-v123-4.21.0.jar;%APP_HOME%\lib\selenium-devtools-v124-4.21.0.jar;%APP_HOME%\lib\selenium-devtools-v125-4.21.0.jar;%APP_HOME%\lib\selenium-firefox-driver-4.21.0.jar;%APP_HOME%\lib\selenium-devtools-v85-4.21.0.jar;%APP_HOME%\lib\selenium-edge-driver-4.21.0.jar;%APP_HOME%\lib\selenium-ie-driver-4.21.0.jar;%APP_HOME%\lib\selenium-safari-driver-4.21.0.jar;%APP_HOME%\lib\selenium-support-4.21.0.jar;%APP_HOME%\lib\selenium-chromium-driver-4.21.0.jar;%APP_HOME%\lib\selenium-remote-driver-4.21.0.jar;%APP_HOME%\lib\selenium-manager-4.21.0.jar;%APP_HOME%\lib\selenium-http-4.21.0.jar;%APP_HOME%\lib\selenium-json-4.21.0.jar;%APP_HOME%\lib\selenium-os-4.21.0.jar;%APP_HOME%\lib\selenium-api-4.21.0.jar;%APP_HOME%\lib\poi-ooxml-lite-5.3.0.jar;%APP_HOME%\lib\xmlbeans-5.2.1.jar;%APP_HOME%\lib\commons-compress-1.26.2.jar;%APP_HOME%\lib\commons-io-2.16.1.jar;%APP_HOME%\lib\curvesapi-1.08.jar;%APP_HOME%\lib\log4j-api-2.23.1.jar;%APP_HOME%\lib\commons-collections4-4.4.jar;%APP_HOME%\lib\httpmime-4.5.13.jar;%APP_HOME%\lib\httpclient-4.5.13.jar;%APP_HOME%\lib\commons-codec-1.17.0.jar;%APP_HOME%\lib\commons-math3-3.6.1.jar;%APP_HOME%\lib\SparseBitSet-1.3.jar;%APP_HOME%\lib\jsoup-1.8.3.jar;%APP_HOME%\lib\sqlite-jdbc-3.8.11.1.jar;%APP_HOME%\lib\freemarker-2.3.23.jar;%APP_HOME%\lib\xml-path-5.2.0.jar;%APP_HOME%\lib\json-path-5.2.0.jar;%APP_HOME%\lib\groovy-json-4.0.1.jar;%APP_HOME%\lib\groovy-xml-4.0.1.jar;%APP_HOME%\lib\rest-assured-common-5.2.0.jar;%APP_HOME%\lib\groovy-4.0.1.jar;%APP_HOME%\lib\hamcrest-2.1.jar;%APP_HOME%\lib\tagsoup-1.2.1.jar;%APP_HOME%\lib\woodstox-core-7.0.0.jar;%APP_HOME%\lib\stax2-api-4.2.2.jar;%APP_HOME%\lib\auto-service-annotations-1.1.1.jar;%APP_HOME%\lib\uri-template-0.10.jar;%APP_HOME%\lib\guava-33.2.0-jre.jar;%APP_HOME%\lib\opentelemetry-semconv-1.25.0-alpha.jar;%APP_HOME%\lib\opentelemetry-exporter-logging-1.38.0.jar;%APP_HOME%\lib\opentelemetry-sdk-extension-autoconfigure-1.38.0.jar;%APP_HOME%\lib\opentelemetry-sdk-extension-autoconfigure-spi-1.38.0.jar;%APP_HOME%\lib\opentelemetry-sdk-1.38.0.jar;%APP_HOME%\lib\opentelemetry-sdk-trace-1.38.0.jar;%APP_HOME%\lib\opentelemetry-sdk-metrics-1.38.0.jar;%APP_HOME%\lib\opentelemetry-sdk-logs-1.38.0.jar;%APP_HOME%\lib\opentelemetry-sdk-common-1.38.0.jar;%APP_HOME%\lib\opentelemetry-api-incubator-1.38.0-alpha.jar;%APP_HOME%\lib\opentelemetry-api-1.38.0.jar;%APP_HOME%\lib\opentelemetry-context-1.38.0.jar;%APP_HOME%\lib\byte-buddy-1.14.15.jar;%APP_HOME%\lib\commons-lang3-3.14.0.jar;%APP_HOME%\lib\httpcore-4.4.13.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\mailapi-1.6.2.jar;%APP_HOME%\lib\joda-time-2.10.5.jar;%APP_HOME%\lib\libphonenumber-8.11.1.jar;%APP_HOME%\lib\msg-simple-1.2.jar;%APP_HOME%\lib\btf-1.3.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\jopt-simple-5.0.4.jar;%APP_HOME%\lib\failsafe-3.3.2.jar;%APP_HOME%\lib\failureaccess-1.0.2.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\checker-qual-3.42.0.jar;%APP_HOME%\lib\error_prone_annotations-2.26.1.jar;%APP_HOME%\lib\commons-exec-1.4.0.jar;%APP_HOME%\lib\rhino-1.7.7.2.jar


@rem Execute qtri_api_ui
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %QTRI_API_UI_OPTS%  -classpath "%CLASSPATH%" demo.App %*

:end
@rem End local scope for the variables with windows NT shell
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem Set variable QTRI_API_UI_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%QTRI_API_UI_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
