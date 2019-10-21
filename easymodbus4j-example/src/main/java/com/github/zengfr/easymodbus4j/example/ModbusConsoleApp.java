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

import java.util.Collection;
import io.netty.channel.Channel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import com.github.zengfr.easymodbus4j.ModbusConsts;
import com.github.zengfr.easymodbus4j.common.util.ConsoleUtil;
import com.github.zengfr.easymodbus4j.common.util.ScheduledUtil;
import com.github.zengfr.easymodbus4j.ModbusConfs;
import com.github.zengfr.easymodbus4j.processor.ModbusMasterResponseProcessor;
import com.github.zengfr.easymodbus4j.processor.ModbusSlaveRequestProcessor;
import com.github.zengfr.easymodbus4j.example.processor.ExampleModbusMasterResponseProcessor;
import com.github.zengfr.easymodbus4j.example.processor.ExampleModbusSlaveRequestProcessor;
import com.github.zengfr.easymodbus4j.example.schedule.ModbusMasterSchedule4ConfigFile;

/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
public class ModbusConsoleApp {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(ModbusConsoleApp.class);

	public static void initAndStart(String[] argsArray) throws Exception {
		ModbusConfig config = ModbusConfig.parse(argsArray);
		start(config);
	}

	public static void start(ModbusConfig cfg) throws Exception {
		ModbusConsts.DEFAULT_UNIT_IDENTIFIER = cfg.unit_IDENTIFIER;
		ModbusConsts.HEARTBEAT=cfg.heartbeat;
		
		ModbusConfs.MASTER_SHOW_DEBUG_LOG = cfg.showDebugLog;
		ModbusConfs.SLAVE_SHOW_DEBUG_LOG = cfg.showDebugLog;
		ModbusConfs.IDLE_TIMEOUT_SECOND = cfg.idleTimeOut;
		ModbusConfs.RESPONS_EFRAME_IGNORE_LENGTH_THRESHOLD= cfg.ignoreLengthThreshold;

		ModbusMasterResponseProcessor masterProcessor = new ExampleModbusMasterResponseProcessor(cfg.transactionIdentifierOffset);
		ModbusSlaveRequestProcessor slaveProcessor = new ExampleModbusSlaveRequestProcessor(cfg.transactionIdentifierOffset);

		ModbusSetup setup = new ModbusSetup();
		setup.initProperties();
		setup.initHandler(masterProcessor, slaveProcessor);

		int port = cfg.port;
		boolean autoSend = cfg.autoSend;
		String host = cfg.host;
		int sleep = cfg.sleep;
		switch (cfg.type) {

		case 6:
			setup.setupServer4RtuMaster(port);
			sendRequests4Auto(autoSend, sleep, setup.getModbusServer().getChannels());
			break;
		case 7:
			setup.setupClient4RtuSlave(host, port);
			break;
		case 8:
			setup.setupClient4RtuMaster(host, port);
			sendRequests4Auto(autoSend, sleep, setup.getModbusClient().getChannels());
			break;
		case 9:
			setup.setupServer4RtuSlave(port);
			break;
		case 5:
			ModbusConfs.SLAVE_SHOW_DEBUG_LOG = false;
			setup.setupServer4RtuMaster(port);
			setup.setupClient4RtuSlave(host, port);
			sendRequests4Auto(autoSend, sleep, setup.getModbusServer().getChannels());
			break;

		case 1:
			setup.setupServer4TcpMaster(port);
			sendRequests4Auto(autoSend, sleep, setup.getModbusServer().getChannels());
			break;
		case 2:
			setup.setupClient4TcpSlave(host, port);
			break;
		case 3:
			setup.setupClient4TcpMaster(host, port);
			sendRequests4Auto(autoSend, sleep, setup.getModbusClient().getChannels());
			break;
		case 4:
			setup.setupServer4TcpSlave(port);
			break;
		default:
			ModbusConfs.SLAVE_SHOW_DEBUG_LOG = false;
			setup.setupServer4TcpMaster(port);
			setup.setupClient4TcpSlave(host, port);
			sendRequests4Auto(autoSend, sleep, setup.getModbusServer().getChannels());
			break;
		}
		Runnable runnable = () -> ConsoleUtil.clearConsole(true);
		ScheduledUtil.getInstance().scheduleAtFixedRate(runnable, sleep * 5);
	}

	protected static void sendRequests4Auto(boolean autoSend, int sleep, Collection<Channel> channels) throws InterruptedException {
		if (autoSend) {
			ModbusMasterSchedule4ConfigFile modbusMasterAutoSender4ConfigFile = new ModbusMasterSchedule4ConfigFile();
			modbusMasterAutoSender4ConfigFile.schedule(channels, sleep);
		}
	}
}
