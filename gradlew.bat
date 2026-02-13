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
@rem SPDX-License-Identifier: Apache-2.0
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  Gradle startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

@rem Determine the directory containing this script.
set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem Ensure DIRNAME is an absolute path.
for %%i in ("%DIRNAME%") do set DIRNAME=%%~fi

@rem Set the application base name (the name of the script without extension).
set APP_BASE_NAME=%~n0

@rem Set the application home directory.
set APP_HOME=%DIRNAME%

@rem --- Java detection ---
@rem Check if JAVA_HOME is set and points to a valid JDK.
if defined JAVA_HOME (
    set JAVA_EXE=%JAVA_HOME:\=/%/bin/java.exe
    if exist "%JAVA_EXE%" goto foundJavaHome
    echo. 1>&2
    echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME% 1>&2
    echo Please set the JAVA_HOME variable in your environment to match the 1>&2
    echo location of your Java installation. 1>&2
    goto fail
)

@rem If JAVA_HOME is not set, try to find java.exe in the system PATH.
set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

@rem If java.exe is not found in PATH either.
echo. 1>&2
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH. 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation, or ensure 'java' is in your PATH. 1>&2
goto fail

:foundJavaHome
@rem Found Java executable via JAVA_HOME.

:execute
@rem --- Execute Gradle ---
@rem Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS environment variables.
@rem These options are passed directly to the JVM.
@rem Removed unnecessary quotes around options.
set DEFAULT_JVM_OPTS=-Xmx64m -Xms64m

@rem Clear CLASSPATH to ensure the wrapper JAR is used exclusively.
set CLASSPATH=

@rem Construct and execute the Gradle command.
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% "-Dorg.gradle.appname=%APP_BASE_NAME%" -classpath "%CLASSPATH%" -jar "%APP_HOME%\\gradle\\wrapper\\gradle-wrapper.jar" %*

:end
@rem End local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" endlocal

@rem Exit with the script's return code.
if %ERRORLEVEL% equ 0 goto mainEnd
goto fail

:mainEnd
exit /b 0

:fail
@rem Set variable GRADLE_EXIT_CONSOLE if you need the _script_ return code instead of
@rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%GRADLE_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:omega
@rem This label is unused but kept for potential future expansion.