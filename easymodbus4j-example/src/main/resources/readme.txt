Supports Function Codes:

Read Coils (FC1)
Read Discrete Inputs (FC2)
Read Holding Registers (FC3)
Read Input Registers (FC4)
Write Single Coil (FC5)
Write Single Register (FC6)
Write Multiple Coils (FC15)
Write Multiple Registers (FC16)
Read/Write Multiple Registers (FC23)

#Example in https://github.com/zengfr/easymodbus4j

<dependency>
<groupId>com.github.zengfr.project</groupId>
<artifactId>easymodbus4j</artifactId>
<version>0.0.1</version>
</dependency>

run startup:
1、unzip file easymodbus4j-release.zip.
2、for modbus master mode:open autosend.txt file in dir or autosend.txt rsourcefile in easymodbus4j.jar 
3、for modbus master mode:edit autosend.txt file
4、start startup.bat.
5、you also can edit *.bat for modbus master/salve mode: .
说明:
1、解压缩zip文件到文件夹
2、java程序 运行不了 则安装jdk8.
3、解压后4个bat文件  对应 服务器master,客户端slave,服务器slave,客户端master.
4、Master模式 可以设置autosend.txt文件，定时发送读写请求。
5、记事本打开bat文件可以编辑相关参数，如定时延时发送时间以及详细日志开关。