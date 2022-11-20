@echo off
rem type,host,port,unit_IDENTIFIER,transactionIdentifierOffset,showDebugLog,idleTimeout,autoSend,sleep,heartbeat,ignoreLengthThreshold,udpPort
java -jar easymodbus4j-example2-0.0.1.jar 6,127.0.0.1,502,1,0,T,0,T,12000,www.mokuai.cn,36,54321
pause
@echo on