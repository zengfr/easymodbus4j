![easymodbus4j运行效果图截屏](https://github.com/zengfr/easymodbus4j/blob/master/easymodbus4j-example/src/main/resources/capture.PNG?raw=true)
# easymodbus4j
easymodbus4j是一个高性能和易用的 Modbus 协议的 Java 实现，基于 Netty 开发，可用于 Modbus协议的Java客户端和服务器开发.
easymodbus4j
A high-performance and ease-of-use implementation of the Modbus protocol written in Java netty support for modbus 8 mode client/server and master/slave.
<pre>
easymodbus4j 特点:
1、Netty NIO high performance高性能.
2、Modbus Function sync/aync 同步/异步非阻塞。
3、Modbus IoT Data Connector Supports工业物联网平台IoT支持。
4、支持Modbus TCP\Modbus RTU protocol两种通信协议.
5、灵活架构,支持8种生产部署模式,自由组合,满足不同生产要求.
6、通用组件包,支持高度自定义接口.
7、完全支持Modbus TCP 4种部署模式: TCP服务器master,TCP客户端slave,TCP服务器slave,TCP客户端master。
8、完全支持Modbus RTU 4种部署模式: RTU服务器master,RTU客户端slave,RTU服务器slave,RTU客户端master。
9、友好的调试以及日志支持bit\bitset\byte\short\int\float\double。
10、Supports Function Codes:
Read Coils (FC1)
Read Discrete Inputs (FC2)
Read Holding Registers (FC3)
Read Input Registers (FC4)
Write Single Coil (FC5)
Write Single Register (FC6)
Write Multiple Coils (FC15)
Write Multiple Registers (FC16)
Read/Write Multiple Registers (FC23)
</pre>
#Example Project Code in https://github.com/zengfr/easymodbus4j

[Repositories Central Sonatype Mvnrepository easymodbus4j](https://mvnrepository.com/artifact/com.github.zengfr/easymodbus4j)
``` 
artifactId/jar:
easymodbus4j-core.jar   	Modbus protocol协议
easymodbus4j-codec.jar  	Modbus 通用编码器解码器
easymodbus4j.jar        	Modbus General/Common公共通用包
easymodbus4j-client.jar 	Modbus client客户端
easymodbus4j-server.jar 	Modbus server服务器端
easymodbus4j-extension.jar  Modbus extension扩展包 ModbusMasterResponseProcessor/ModbusSlaveRequestProcessor interface
``` 
``` 
快速开发Quick Start:
第一步step1 ,import jar:
maven:
<dependency>
<groupId>com.github.zengfr</groupId>
<artifactId>easymodbus4j-client</artifactId>
<version>0.0.5</version>
</dependency>
<dependency>
<groupId>com.github.zengfr</groupId>
<artifactId>easymodbus4j-server</artifactId>
<version>0.0.5</version>
</dependency>
<dependency>
<groupId>com.github.zengfr</groupId>
<artifactId>easymodbus4j-extension</artifactId>
<version>0.0.5</version>
</dependency>

第二步step2, implement handler:
2.1 if master 
      实现implement ResponseHandler接口 see easymodbus4j-example:ModbusMasterResponseHandler.java 
  or 实现implement ModbusMasterResponseProcessor 接口 use new ModbusMasterResponseHandler(responseProcessor); 
  
2.2 if slave 
    实现implement RequestHandler接口 see easymodbus4j-example:ModbusSlaveRequestHandler.java 
 or 实现implement ModbusSlaveRequestProcessor 接口 use new ModbusSlaveRequestHandler(reqProcessor); 

第三步step3, select one master/slave client/server mode:
modbusServer = ModbusServerTcpFactory.getInstance().createServer4Master(port, responseHandler);
modbusClient = ModbusClientTcpFactory.getInstance().createClient4Slave(host,port, requestHandler);

modbusClient = ModbusClientTcpFactory.getInstance().createClient4Master(host, port, responseHandler);
modbusServer = ModbusServerTcpFactory.getInstance().createServer4Slave(port, requestHandler);

modbusServer = ModbusServerRtuFactory.getInstance().createServer4Master(port, responseHandler);
modbusClient = ModbusClientRtuFactory.getInstance().createClient4Slave(host,port, requestHandler);

modbusClient = ModbusClientRtuFactory.getInstance().createClient4Master(host, port, responseHandler);
modbusServer = ModbusServerRtuFactory.getInstance().createServer4Slave(port, requestHandler);

第四步step4:
4.1 how to send a request ? to send data
Thread.sleep(3*1000);// sleep 3s before,when client or server open connection is async;
Channel  channel =  client.getChannel());
Channel  channel =  server.getChannelsBy(...));
ChannelSender sender = ChannelSenderFactory.getInstance().get(channel);
ChannelSender sender2 = new ChannelSender(channel,unitId,protocolIdentifier);
sender.readCoils(...)
sender.readDiscreteInputs(...)
sender.writeSingleRegister(...)

4.2 how to process request/response? to receive data
see code in processResponseFrame mothod in  ModbusMasterResponseHandler.java or ModbusMasterResponseProcessor.java
public void processResponseFrame(Channel channel, int unitId, AbstractFunction reqFunc, ModbusFunction respFunc) {
		if (respFunc instanceof ReadCoilsResponse) {
			ReadCoilsResponse resp = (ReadCoilsResponse) respFunc;
			ReadCoilsRequest req = (ReadCoilsRequest) reqFunc;
			//process business logic for req/resp
		}
};
4.3 how to get response to byteArray for custom decode by yourself?
see code in processResponseFrame mothod in  ModbusMasterResponseHandler.java or ModbusMasterResponseProcessor.java
public void processResponseFrame(Channel channel, int unitId, AbstractFunction reqFunc, ModbusFunction respFunc) {
		if (respFunc instanceof ReadDiscreteInputsResponse) {
			ReadDiscreteInputsResponse resp = (ReadDiscreteInputsResponse) respFunc;
			byte[] resutArray = resp.getInputStatus().toByteArray();
		}
};	
4.4 how to show log? 
see ModbusMasterResponseHandler.java in example project.
ModbusFrameUtil.showFrameLog(logger, channel, frame);

4.5 how to custom a client/server advance by yourself?
ModbusChannelInitializer modbusChannelInitializer=...;
ModbusServerTcpFactory.getInstance().createServer4Master(port,modbusChannelInitializer);
``` 
#Example Project Code [example1](https://github.com/zengfr/easymodbus4j/tree/master/easymodbus4j-example/src/main/java/com/github/zengfr/easymodbus4j/example)
[example3](https://github.com/zengfr/easymodbus4j/tree/master/easymodbus4j-example/src/main/java/com/github/zengfr/easymodbus4j/example3)

[other example1](https://gitee.com/zengfr/easymodbus4j/tree/master/easymodbus4j-example/src/main/java/com/github/zengfr/easymodbus4j/example)
[other example3](https://gitee.com/zengfr/easymodbus4j/tree/master/easymodbus4j-example/src/main/java/com/github/zengfr/easymodbus4j/example3)
<pre>
Example run startup:
1、unzip file easymodbus4j-example-0.0.5-release.zip.
2、for modbus master mode:open autosend.txt file in dir or autosend.txt rsourcefile in easymodbus4j-example-0.0.5.jar 
3、for modbus master mode:edit autosend.txt file
4、start startup.bat.
5、you also can edit *.bat for modbus master/salve mode: .
说明:
1、解压缩zip文件到文件夹
2、java程序 运行不了 则安装jdk8.
3、解压后8个bat文件  对应TCP/RTU 服务器master,客户端slave,服务器slave,客户端master 8种模式.
4、Master模式 可以设置autosend.txt文件，定时发送读写请求。
5、记事本打开bat文件可以编辑相关参数，如定时延时发送时间以及详细日志开关。
</pre>
capture运行效果图截屏:
![easymodbus4j运行效果图截屏1](https://github.com/zengfr/easymodbus4j/blob/master/easymodbus4j-example/src/main/resources/capture.PNG?raw=true)
![easymodbus4j运行效果图截屏2](https://github.com/zengfr/easymodbus4j/blob/master/easymodbus4j-example/src/main/resources/capture2.PNG?raw=true)
![easymodbus4j运行效果图截屏3](https://github.com/zengfr/easymodbus4j/blob/master/easymodbus4j-example/src/main/resources/capture3.PNG?raw=true)
![easymodbus4j运行效果图截屏4](https://github.com/zengfr/easymodbus4j/blob/master/easymodbus4j-example/src/main/resources/capture4.PNG?raw=true)

capture运行效果图截屏:
![easymodbus4j运行效果图截屏1](https://gitee.com/zengfr/easymodbus4j/raw/master/easymodbus4j-example/src/main/resources/capture.PNG?raw=true)
![easymodbus4j运行效果图截屏2](https://gitee.com/zengfr/easymodbus4j/raw/master/easymodbus4j-example/src/main/resources/capture2.PNG?raw=true)
![easymodbus4j运行效果图截屏3](https://gitee.com/zengfr/easymodbus4j/raw/master/easymodbus4j-example/src/main/resources/capture3.PNG?raw=true)
![easymodbus4j运行效果图截屏4](https://gitee.com/zengfr/easymodbus4j/raw/master/easymodbus4j-example/src/main/resources/capture4.PNG?raw=true)
