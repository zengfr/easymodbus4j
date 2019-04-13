package com.github.zengfr.easymodbus4j.example;

import com.github.zengfr.easymodbus4j.client.ModbusClient;
import com.github.zengfr.easymodbus4j.client.ModbusClientFactory;
import com.github.zengfr.easymodbus4j.handle.ModbusRequestHandler;
import com.github.zengfr.easymodbus4j.handle.ModbusResponseHandler;
import com.github.zengfr.easymodbus4j.server.ModbusServer;
import com.github.zengfr.easymodbus4j.server.ModbusServerFactory;

public class ModbusSetup {
	protected static final int sleep = 1000;
	protected ModbusClient modbusClient;
	protected ModbusServer modbusServer;

	protected ModbusRequestHandler requestHandler;
	protected ModbusResponseHandler responseHandler;

	protected void init() throws Exception {
		requestHandler = new ModbusSlaveRequestHandler();
		responseHandler = new ModbusMasterResponseHandler();
		// System.setProperty("io.netty.noUnsafe", "false");
		// System.setProperty("io.netty.tryReflectionSetAccessible", "true");
		// InternalLoggerFactory.setDefaultFactory(new Log4JLoggerFactory());
		// ReferenceCountUtil.release(byteBuf);
		// ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);
	}

	public void setUpServer4Master(int port) throws Exception {
		modbusServer = ModbusServerFactory.getInstance().createServer4Master(port, responseHandler);
	}

	public void setUpServer4Slave(int port) throws Exception {
		modbusServer = ModbusServerFactory.getInstance().createServer4Slave(port, requestHandler);

	}

	public void setUpClient4Slave(String host, int port) throws Exception {
		Thread.sleep(sleep);
		modbusClient = ModbusClientFactory.getInstance().createClient4Slave(host, port, requestHandler);
	}

	public void setUpClient4Master(String host, int port) throws Exception {
		Thread.sleep(sleep);
		modbusClient = ModbusClientFactory.getInstance().createClient4Master(host, port, responseHandler);
	}

	public void setUpClient4Slave(int port) throws Exception {
		Thread.sleep(sleep);
		modbusClient = ModbusClientFactory.getInstance().createClient4Slave(port, requestHandler);
	}

	public void setUpClient4Master(int port) throws Exception {
		Thread.sleep(sleep);
		modbusClient = ModbusClientFactory.getInstance().createClient4Master(port, responseHandler);
	}
}
