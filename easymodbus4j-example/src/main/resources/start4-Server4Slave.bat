@echo off
rem int port, boolean showDebugLog, int type, boolean autoSend, int sleep, String host
java -jar easymodbus4j-example-0.0.1.jar 502,T,4,F,15000,
pause
@echo on