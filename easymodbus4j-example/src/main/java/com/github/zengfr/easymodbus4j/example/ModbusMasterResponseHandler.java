package com.github.zengfr.easymodbus4j.example;

import com.github.zengfr.easymodbus4j.handle.ModbusResponseHandler;
import com.github.zengfr.easymodbus4j.protocol.ModbusFrame;
import com.github.zengfr.easymodbus4j.util.LogUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

@ChannelHandler.Sharable
public class ModbusMasterResponseHandler extends ModbusResponseHandler {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(ModbusMasterResponseHandler.class);

	@Override
	protected void processResponseFrame(Channel channel, ModbusFrame frame) {
		LogUtil.showLog(logger, channel, frame);
	}
}
