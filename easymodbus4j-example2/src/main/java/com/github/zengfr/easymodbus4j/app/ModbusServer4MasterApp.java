package com.github.zengfr.easymodbus4j.app;

import java.util.Collection;

import com.github.zengfr.easymodbus4j.ModbusConstants;
import com.github.zengfr.easymodbus4j.app.handle.CustomModbusMasterResponseHandler;
import com.github.zengfr.easymodbus4j.app.plugin.DeviceCommandPluginRegister;
import com.github.zengfr.easymodbus4j.app.plugin.DeviceRepositoryPluginRegister;
import com.github.zengfr.easymodbus4j.app.plugin.impl.DeviceCommandV1PluginImpl;
import com.github.zengfr.easymodbus4j.app.plugin.impl.DeviceRepositoryV1PluginImpl;
import com.github.zengfr.easymodbus4j.app.schedule.ModbusMasterSchedule4All;
import com.github.zengfr.easymodbus4j.app.schedule.ModbusMasterSchedule4DeviceId;
import com.github.zengfr.easymodbus4j.app.server.udp.UdpServer;
import com.github.zengfr.easymodbus4j.app.server.udp.UdpServerHandler4SendToServer;
import com.github.zengfr.easymodbus4j.example.ModbusConfig;
import com.github.zengfr.easymodbus4j.example.ModbusSetup;
import com.github.zengfr.easymodbus4j.util.ConsoleUtil;
import com.github.zengfr.easymodbus4j.util.ScheduledUtil;

import io.netty.channel.Channel;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
public class ModbusServer4MasterApp extends ModbusSetup {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(ModbusServer4MasterApp.class.getSimpleName());

	public static void init(String[] argsArray) throws Exception {

		ModbusConfig config = ModbusConfig.parse(argsArray);
		start(config);

	}

	public static void start(ModbusConfig cfg) throws Exception {
		ModbusConstants.MASTER_SHOW_DEBUG_LOG = cfg.showDebugLog;
		ModbusConstants.DEFAULT_UNIT_IDENTIFIER = cfg.unit_IDENTIFIER;
		DeviceCommandPluginRegister.getInstance().reg(DeviceCommandV1PluginImpl.class.newInstance());
		DeviceRepositoryPluginRegister.getInstance().reg(DeviceRepositoryV1PluginImpl.class.newInstance());

		ModbusSetup setup = new ModbusSetup();
		setup.setHandler(null, new CustomModbusMasterResponseHandler(cfg.transactionIdentifierOffset));
		setup.setupServer4Master(cfg.port);
		Collection<Channel> channels = setup.getModbusServer().getChannels();

		UdpServer udpServer = new UdpServer();
		SimpleChannelInboundHandler<DatagramPacket> handler = new UdpServerHandler4SendToServer(channels);
		udpServer.setup(cfg.udpPort, handler);
		int sleep = cfg.sleep;
		if (cfg.autoSend) {
			Thread.sleep(sleep);
			ModbusMasterSchedule4DeviceId modbusMasterSchedule4DeviceId = new ModbusMasterSchedule4DeviceId();
			modbusMasterSchedule4DeviceId.run(channels);
			modbusMasterSchedule4DeviceId.schedule(channels, sleep * 5);

			ModbusMasterSchedule4All modbusMasterSchedule4All = new ModbusMasterSchedule4All();
			modbusMasterSchedule4All.schedule(channels, sleep);
		}
		Runnable runnable = () -> ConsoleUtil.clearConsole(true);
		ScheduledUtil.scheduleWithFixedDelay(runnable, sleep * 5);
	}
}
