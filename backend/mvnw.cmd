@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements. See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership. The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License. You may obtain a copy of the License at
@REM
@REM https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied. See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script, version 3.3.2
@REM
@REM Required ENV vars:
@REM JAVA_HOME - location of a JDK home dir
@REM
@REM Optional ENV vars
@REM MAVEN_BATCH_ECHO - set to 'on' to enable the echoing of the batch commands
@REM MAVEN_BATCH_PAUSE - set to 'on' to wait for a keystroke before ending
@REM MAVEN_OPTS - parameters passed to the Java VM when running Maven
@REM     e.g. to debug Maven itself, use
@REM set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
@REM MAVEN_SKIP_RC - flag to disable loading of mavenrc files
@REM ----------------------------------------------------------------------------

@IF "%MAVEN_BATCH_ECHO%"=="on" echo %MAVEN_BATCH_ECHO%

@REM Set %HOME% to equivalent of $HOME
@IF "%HOME%"=="" (SET "HOME=%HOMEDRIVE%%HOMEPATH%")

@SET MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%
@IF NOT "%MAVEN_PROJECTBASEDIR%"=="" goto endDetectBaseDir

@SET EXEC_DIR=%CD%
@SET WDIR=%EXEC_DIR%
:findBaseDir
@IF EXIST "%WDIR%"\.mvn goto baseDirFound
@SET WDIR_PARENT="%WDIR%\.."
@IF /I "%WDIR%"=="%WDIR_PARENT%" goto baseDirNotFound
@SET WDIR=%WDIR_PARENT%
@goto findBaseDir
:baseDirFound
@SET MAVEN_PROJECTBASEDIR=%WDIR%
@goto endDetectBaseDir
:baseDirNotFound
@SET MAVEN_PROJECTBASEDIR=%EXEC_DIR%
:endDetectBaseDir
@SET MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR:"=%

@SET WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar
@SET WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

@SET WRAPPER_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"

FOR /F "usebackq tokens=1,2 delims==" %%A IN ("%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties") DO (
    IF "%%A"=="wrapperUrl" SET WRAPPER_URL=%%B
)

@IF EXIST "%WRAPPER_JAR%" (
    IF "%MVNW_VERBOSE%"=="true" (
        ECHO Found %WRAPPER_JAR%
    )
) ELSE (
    IF NOT "%MVNW_REPOURL%"=="" SET WRAPPER_URL="%MVNW_REPOURL%/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"
    IF "%MVNW_VERBOSE%"=="true" (
        ECHO Couldn't find %WRAPPER_JAR%, downloading it ...
        ECHO Downloading from: %WRAPPER_URL%
    )

    powershell -Command "&{"^
      "$webclient = new-object System.Net.WebClient;"^
      "if (-not ([string]::IsNullOrEmpty('%MVNW_USERNAME%') -and [string]::IsNullOrEmpty('%MVNW_PASSWORD%'))) {"^
      "$webclient.Credentials = new-object System.Net.NetworkCredential('%MVNW_USERNAME%', '%MVNW_PASSWORD%');"^
      "}"^
      "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; $webclient.DownloadFile('%WRAPPER_URL%', '%WRAPPER_JAR%')"^
      "}"
    IF "%MVNW_VERBOSE%"=="true" (
        ECHO Finished downloading %WRAPPER_JAR%
    )
)

@REM Start MAVEN_OPTS pointing to the wrapper jar
@SET MAVEN_OPTS_EXTRA="-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" "-classpath" "%WRAPPER_JAR%" "%WRAPPER_LAUNCHER%" %MAVEN_CONFIG%

@IF DEFINED JAVA_HOME goto runMaven
@IF NOT "%JAVA_HOME%"=="" (
    SET JAVA_HOME=%JAVA_HOME:"=%
    goto runMaven
)

@ECHO Error: JAVA_HOME not found in your environment.
@ECHO Please set the JAVA_HOME variable in your environment to match the
@ECHO location of your Java installation.
@goto end

:runMaven
@SET "JAVA_HOME=%JAVA_HOME%"
@"%JAVA_HOME%\bin\java.exe" %JVM_CONFIG_MAVEN_OPTS% %MAVEN_OPTS% %MAVEN_OPTS_EXTRA% %MAVEN_ARGS% %WRAPPER_EXTRA_ARGS% %*
:end
@IF "%MAVEN_BATCH_PAUSE%"=="on" pause
