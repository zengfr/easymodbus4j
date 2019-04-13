@echo off
rem type,host,port,unit_IDENTIFIER,transactionIdentifierOffset,showDebugLog,autoSend,sleep,udpPort
java -jar easymodbus4j-example2-0.0.1.jar 0,127.0.0.1,502,1,0,T,T,25000,54321
pause
@echo on