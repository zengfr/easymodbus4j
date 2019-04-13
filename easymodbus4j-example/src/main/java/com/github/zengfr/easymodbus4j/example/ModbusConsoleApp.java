package com.github.zengfr.easymodbus4j.example;

import java.util.Collection;

import com.github.zengfr.easymodbus4j.ModbusConstants;
import com.github.zengfr.easymodbus4j.util.ParseStringUtil;

import io.netty.channel.Channel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class ModbusConsoleApp extends ModbusSetup {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(ModbusConsoleApp.class);

	public static void main(String[] args) throws Exception {
		if (args == null || args.length <= 0)
			args = new String[] { "" };
		args = args[0].split("[,;|]");
		int port = ParseStringUtil.parseInt(args, 0, ModbusConstants.DEFAULT_MODBUS_PORT);
		boolean showDebugLog = ParseStringUtil.parseBoolean(args, 1, true);
		int type = ParseStringUtil.parseInt(args, 2, 0);
		boolean autoSend = ParseStringUtil.parseBoolean(args, 3, true);
		int sleep = ParseStringUtil.parseInt(args, 4, 1000 * 15);
		String host = ParseStringUtil.parseString(args, 5, ModbusConstants.DEFAULT_HOTST_IP);
		start(port, showDebugLog, type, autoSend, sleep, host);
	}

	public static void start(int port, boolean showDebugLog, int type, boolean autoSend, int sleep, String host)
			throws Exception {
		logger.info(String.format("port:%s;showDebugLog:%s;type:%s;autoSend:%s;sleep:%s;host:%s;", port, showDebugLog,
				type, autoSend, sleep, host));
		ModbusConstants.MASTER_SHOW_DEBUG_LOG = showDebugLog;
		ModbusConstants.SLAVE_SHOW_DEBUG_LOG = showDebugLog;
		ModbusConsoleApp console = new ModbusConsoleApp();
		console.init();
		switch (type) {
		case 1:
			console.setUpServer4Master(port);
			sendRequests4Auto(autoSend, sleep, console.modbusServer.getChannels());
			break;
		case 2:
			console.setUpClient4Slave(host, port);
			break;
		case 3:
			console.setUpClient4Master(port);
			sendRequests4Auto(autoSend, sleep, console.modbusServer.getChannels());
			break;
		case 4:
			console.setUpClient4Slave(host, port);
			break;
		default:
			ModbusConstants.SLAVE_SHOW_DEBUG_LOG = false;
			console.setUpServer4Master(port);
			console.setUpClient4Slave(host, port);
			sendRequests4Auto(autoSend, sleep, console.modbusServer.getChannels());
			break;
		}
	}

	protected static void sendRequests4Auto(boolean autoSend, int sleep, Collection<Channel> channels)
			throws InterruptedException {
		if (autoSend)
			ModbusMasterAutoSender.sendRequests4Auto(channels, sleep);
	}
}
