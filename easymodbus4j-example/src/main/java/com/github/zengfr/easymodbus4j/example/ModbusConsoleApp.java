package com.github.zengfr.easymodbus4j.example;

import java.util.Collection;

import com.github.zengfr.easymodbus4j.ModbusConstants;
import com.github.zengfr.easymodbus4j.example.schedule.ModbusMasterSchedule4ConfigFile;
import com.github.zengfr.easymodbus4j.util.ConsoleUtil;
import com.github.zengfr.easymodbus4j.util.ScheduledUtil;

import io.netty.channel.Channel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
public class ModbusConsoleApp extends ModbusSetup {
	private static final InternalLogger logger = InternalLoggerFactory
			.getInstance(ModbusConsoleApp.class.getSimpleName());

	public static void init(String[] argsArray) throws Exception {
		ModbusConfig config = ModbusConfig.parse(argsArray);
		start(config);
	}

	public static void start(ModbusConfig cfg) throws Exception {
		ModbusConstants.MASTER_SHOW_DEBUG_LOG = cfg.showDebugLog;
		ModbusConstants.SLAVE_SHOW_DEBUG_LOG = cfg.showDebugLog;
		ModbusConstants.DEFAULT_UNIT_IDENTIFIER = cfg.unit_IDENTIFIER;

		ModbusConsoleApp console = new ModbusConsoleApp();
		console.init(cfg.transactionIdentifierOffset);
		int port = cfg.port;
		boolean autoSend = cfg.autoSend;
		String host = cfg.host;
		int sleep = cfg.sleep;
		switch (cfg.type) {
		case 1:
			console.setupServer4Master(port);
			sendRequests4Auto(autoSend, sleep, console.getModbusServer().getChannels());
			break;
		case 2:
			console.setupClient4Slave(host, port);
			break;
		case 3:
			console.setupClient4Master(host, port);
			sendRequests4Auto(autoSend, sleep, console.getModbusClient().getChannels());
			break;
		case 4:
			console.setupServer4Slave(port);
			break;
		default:
			ModbusConstants.SLAVE_SHOW_DEBUG_LOG = false;
			console.setupServer4Master(port);
			console.setupClient4Slave(host, port);
			sendRequests4Auto(autoSend, sleep, console.getModbusServer().getChannels());
			break;
		}
		Runnable runnable = () -> ConsoleUtil.clearConsole(true);
		ScheduledUtil.scheduleWithFixedDelay(runnable, sleep * 5);
	}

	protected static void sendRequests4Auto(boolean autoSend, int sleep, Collection<Channel> channels)
			throws InterruptedException {
		if (autoSend) {
			ModbusMasterSchedule4ConfigFile modbusMasterAutoSender4ConfigFile = new ModbusMasterSchedule4ConfigFile();
			modbusMasterAutoSender4ConfigFile.schedule(channels, sleep);
		}
	}
}
