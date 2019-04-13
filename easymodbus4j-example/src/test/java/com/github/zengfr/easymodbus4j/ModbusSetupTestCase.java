package com.github.zengfr.easymodbus4j;

import com.github.zengfr.easymodbus4j.channel.ChannelSender;
import com.github.zengfr.easymodbus4j.example.ModbusSetup;
import com.github.zengfr.easymodbus4j.func.response.ReadHoldingRegistersResponse;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class ModbusSetupTestCase extends ModbusSetup {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(ModbusSetupTestCase.class);

	public void testServer4Master(int sleep) throws Exception {

		String key = modbusClient.getChannel().localAddress().toString();
		ChannelSender sender = new ChannelSender(modbusServer.getChannel(key));
		for (int i = 0; i < 999; i++) {
			ReadHoldingRegistersResponse readHoldingRegisters = sender.readHoldingRegisters(18, 6);
			logger.info("" + readHoldingRegisters);
			Thread.sleep(sleep);
		}

	}

	public void testClient4Master(int sleep) throws Exception {

		ChannelSender sender = new ChannelSender(modbusClient.getChannel());
		for (int i = 0; i < 999; i++) {
			ReadHoldingRegistersResponse readHoldingRegisters = sender.readHoldingRegisters(18, 6);
			logger.info("" + readHoldingRegisters);
			Thread.sleep(sleep);
		}
	}
}
