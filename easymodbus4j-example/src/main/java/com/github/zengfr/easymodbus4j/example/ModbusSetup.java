package com.github.zengfr.easymodbus4j.example;

import com.github.zengfr.easymodbus4j.client.ModbusClient;
import com.github.zengfr.easymodbus4j.client.ModbusClientFactory;
import com.github.zengfr.easymodbus4j.example.handler.ModbusMasterResponseHandler;
import com.github.zengfr.easymodbus4j.example.handler.ModbusSlaveRequestHandler;
import com.github.zengfr.easymodbus4j.handle.ModbusRequestHandler;
import com.github.zengfr.easymodbus4j.handle.ModbusResponseHandler;
import com.github.zengfr.easymodbus4j.server.ModbusServer;
import com.github.zengfr.easymodbus4j.server.ModbusServerFactory;

public class ModbusSetup {
	private static final int sleep = 1000;
	protected ModbusClient modbusClient;
	protected ModbusServer modbusServer;

	protected ModbusRequestHandler requestHandler;
	protected ModbusResponseHandler responseHandler;

	public ModbusSetup() {

	}
	public ModbusClient getModbusClient() {
		return this.modbusClient;
	}
	public ModbusServer getModbusServer() {
		return this.modbusServer;
	}

	public void init(short transactionIdentifierOffset) throws Exception {
		requestHandler = new ModbusSlaveRequestHandler(transactionIdentifierOffset);
		responseHandler = new ModbusMasterResponseHandler(transactionIdentifierOffset);
		// System.setProperty("io.netty.noUnsafe", "false");
		// System.setProperty("io.netty.tryReflectionSetAccessible", "true");
		// InternalLoggerFactory.setDefaultFactory(new Log4JLoggerFactory());
		// ReferenceCountUtil.release(byteBuf);
		// ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);
	}
	public void setHandler(ModbusRequestHandler requestHandler, ModbusResponseHandler responseHandler) throws Exception {
		this.requestHandler = requestHandler;
		this.responseHandler = responseHandler;
	}

	public void setupServer4Master(int port) throws Exception {
		modbusServer = ModbusServerFactory.getInstance().createServer4Master(port, responseHandler);
	}

	public void setupServer4Slave(int port) throws Exception {
		modbusServer = ModbusServerFactory.getInstance().createServer4Slave(port, requestHandler);

	}

	public void setupClient4Slave(String host, int port) throws Exception {
		Thread.sleep(sleep);
		modbusClient = ModbusClientFactory.getInstance().createClient4Slave(host, port, requestHandler);
	}

	public void setupClient4Master(String host, int port) throws Exception {
		Thread.sleep(sleep);
		modbusClient = ModbusClientFactory.getInstance().createClient4Master(host, port, responseHandler);
	}
}
