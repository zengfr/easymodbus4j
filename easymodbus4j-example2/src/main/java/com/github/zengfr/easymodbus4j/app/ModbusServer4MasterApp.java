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
package com.github.zengfr.easymodbus4j.app;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.zengfr.easymodbus4j.ModbusConsts;
import com.github.zengfr.easymodbus4j.app.plugin.DeviceCommandPluginRegister;
import com.github.zengfr.easymodbus4j.app.plugin.DeviceRepositoryPluginRegister;
import com.github.zengfr.easymodbus4j.app.plugin.impl.DeviceCommandV1PluginImpl;
import com.github.zengfr.easymodbus4j.app.plugin.impl.DeviceRepositoryV1PluginImpl;
import com.github.zengfr.easymodbus4j.app.processor.CustomModbusMasterResponseProcessor;
import com.github.zengfr.easymodbus4j.app.schedule.ModbusMasterSchedule4All;
import com.github.zengfr.easymodbus4j.app.schedule.ModbusMasterSchedule4DeviceId;
import com.github.zengfr.easymodbus4j.app.server.udp.UdpServer;
import com.github.zengfr.easymodbus4j.app.server.udp.UdpServerHandler4SendToServer;
import com.github.zengfr.easymodbus4j.cache.ModebusFrameCache;
import com.github.zengfr.easymodbus4j.common.util.ConsoleUtil;
import com.github.zengfr.easymodbus4j.common.util.ScheduledUtil;
import com.github.zengfr.easymodbus4j.ModbusConfs;
import com.github.zengfr.easymodbus4j.example.ModbusConfig;
import com.github.zengfr.easymodbus4j.example.ModbusSetup;
import com.github.zengfr.easymodbus4j.processor.ModbusMasterResponseProcessor;

import io.netty.channel.Channel;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
public class ModbusServer4MasterApp {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(ModbusServer4MasterApp.class.getSimpleName());

	public static void initAndStart(String[] argsArray) throws Exception {

		ModbusConfig config = ModbusConfig.parse(argsArray);
		start(config);

	}

	public static void start(ModbusConfig cfg) throws Exception {
		ModbusConsts.DEFAULT_UNIT_IDENTIFIER = cfg.unit_IDENTIFIER;
		ModbusConsts.HEARTBEAT=cfg.heartbeat;
		
		ModbusConfs.MASTER_SHOW_DEBUG_LOG = cfg.showDebugLog;
		ModbusConfs.IDLE_TIMEOUT_SECOND = cfg.idleTimeOut;
		ModbusConfs.RESPONS_EFRAME_IGNORE_LENGTH_THRESHOLD= cfg.ignoreLengthThreshold;
		DeviceCommandPluginRegister.getInstance().reg(DeviceCommandV1PluginImpl.class.newInstance());
		DeviceRepositoryPluginRegister.getInstance().reg(DeviceRepositoryV1PluginImpl.class.newInstance());

		ModbusMasterResponseProcessor masterProcessor = new CustomModbusMasterResponseProcessor(cfg.transactionIdentifierOffset);

		ModbusSetup setup = new ModbusSetup();
		setup.initProperties();
		setup.initHandler(masterProcessor, null);

		switch (cfg.type) {
		case 6:
			setup.setupServer4RtuMaster(cfg.port);
			break;
		default:
			setup.setupServer4TcpMaster(cfg.port);
			break;
		}

		Collection<Channel> channels = setup.getModbusServer().getChannels();

		UdpServer udpServer = new UdpServer();
		SimpleChannelInboundHandler<DatagramPacket> handler = new UdpServerHandler4SendToServer(channels);
		udpServer.setup(cfg.udpPort, handler);
		int sleep = cfg.sleep;
		logger.info(String.format("autoSend:%s sleep:%s ms", cfg.autoSend,sleep));
		if (cfg.autoSend) {
			Thread.sleep(sleep);
			
			ModbusMasterSchedule4DeviceId modbusMasterSchedule4DeviceId = new ModbusMasterSchedule4DeviceId();
			modbusMasterSchedule4DeviceId.schedule(channels, sleep *4);
			modbusMasterSchedule4DeviceId.run(channels);

			ModbusMasterSchedule4All modbusMasterSchedule4All = new ModbusMasterSchedule4All();
			modbusMasterSchedule4All.schedule(channels, sleep);
		}
		Runnable runnable = () -> ConsoleUtil.clearConsole(true);
		new ScheduledUtil("clearConsole",1).scheduleAtFixedRate(runnable, sleep * 6);
	}
}
