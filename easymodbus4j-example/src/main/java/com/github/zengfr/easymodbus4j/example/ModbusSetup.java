/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.zengfr.easymodbus4j.example;

import com.github.zengfr.easymodbus4j.client.ModbusClient;
import com.github.zengfr.easymodbus4j.client.ModbusClientRtuFactory;
import com.github.zengfr.easymodbus4j.client.ModbusClientTcpFactory;
import com.github.zengfr.easymodbus4j.handle.impl.ModbusMasterResponseHandler;
import com.github.zengfr.easymodbus4j.handle.impl.ModbusSlaveRequestHandler;
import com.github.zengfr.easymodbus4j.handler.ModbusRequestHandler;
import com.github.zengfr.easymodbus4j.handler.ModbusResponseHandler;
import com.github.zengfr.easymodbus4j.processor.ModbusMasterResponseProcessor;
import com.github.zengfr.easymodbus4j.processor.ModbusSlaveRequestProcessor;
import com.github.zengfr.easymodbus4j.server.ModbusServer;
import com.github.zengfr.easymodbus4j.server.ModbusServerRtuFactory;
import com.github.zengfr.easymodbus4j.server.ModbusServerTcpFactory;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;
/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
public class ModbusSetup {
	private static final int sleep = 1000;
	private ModbusClient modbusClient;
	private ModbusServer modbusServer;

	private ModbusRequestHandler requestHandler;
	private ModbusResponseHandler responseHandler;

	public ModbusSetup() {

	}

	public ModbusClient getModbusClient() {
		return this.modbusClient;
	}

	public ModbusServer getModbusServer() {
		return this.modbusServer;
	}

	public void initProperties() throws Exception {
		InternalLoggerFactory.setDefaultFactory(Slf4JLoggerFactory.INSTANCE);
		System.setProperty("io.netty.tryReflectionSetAccessible", "true");
		// System.setProperty("io.netty.noUnsafe", "false");
		// ReferenceCountUtil.release(byteBuf);
		// ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);
	}

	public void initHandler(ModbusResponseHandler responseHandler, ModbusRequestHandler requestHandler) throws Exception {
		this.requestHandler = requestHandler;
		this.responseHandler = responseHandler;
	}

	public void initHandler(ModbusMasterResponseProcessor masterProcessor, ModbusSlaveRequestProcessor slaveProcessor) throws Exception {
		this.requestHandler = new ModbusSlaveRequestHandler(slaveProcessor);
		this.responseHandler = new ModbusMasterResponseHandler(masterProcessor);
	}

	public void setupServer4TcpMaster(int port) throws Exception {
		modbusServer = ModbusServerTcpFactory.getInstance().createServer4Master(port, responseHandler);
	}

	public void setupServer4TcpSlave(int port) throws Exception {
		modbusServer = ModbusServerTcpFactory.getInstance().createServer4Slave(port, requestHandler);

	}

	public void setupClient4TcpSlave(String host, int port) throws Exception {
		Thread.sleep(sleep);
		modbusClient = ModbusClientTcpFactory.getInstance().createClient4Slave(host, port, requestHandler);
	}

	public void setupClient4TcpMaster(String host, int port) throws Exception {
		Thread.sleep(sleep);
		modbusClient = ModbusClientTcpFactory.getInstance().createClient4Master(host, port, responseHandler);
	}

	public void setupServer4RtuMaster(int port) throws Exception {
		modbusServer = ModbusServerRtuFactory.getInstance().createServer4Master(port, responseHandler);
	}

	public void setupServer4RtuSlave(int port) throws Exception {
		modbusServer = ModbusServerRtuFactory.getInstance().createServer4Slave(port, requestHandler);

	}

	public void setupClient4RtuSlave(String host, int port) throws Exception {
		Thread.sleep(sleep);
		modbusClient = ModbusClientRtuFactory.getInstance().createClient4Slave(host, port, requestHandler);
	}

	public void setupClient4RtuMaster(String host, int port) throws Exception {
		Thread.sleep(sleep);
		modbusClient = ModbusClientRtuFactory.getInstance().createClient4Master(host, port, responseHandler);
	}
}
