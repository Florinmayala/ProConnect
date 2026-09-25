@ECHO OFF
SETLOCAL
SET DIRNAME=%~dp0
IF "%JAVA_HOME%"=="" (
  SET JAVA_EXE=java.exe
) ELSE (
  SET JAVA_EXE=%JAVA_HOME%\bin\java.exe
)
"%JAVA_EXE%" -classpath "%DIRNAME%gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
SET EXIT_CODE=%ERRORLEVEL%
ENDLOCAL & EXIT /B %EXIT_CODE%
