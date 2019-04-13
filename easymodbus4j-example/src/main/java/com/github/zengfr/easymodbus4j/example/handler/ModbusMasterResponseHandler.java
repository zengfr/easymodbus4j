package com.github.zengfr.easymodbus4j.example.handler;

import com.github.zengfr.easymodbus4j.handle.ModbusResponseHandler;
import com.github.zengfr.easymodbus4j.protocol.ModbusFrame;
import com.github.zengfr.easymodbus4j.util.ModbusFrameUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

@ChannelHandler.Sharable
public class ModbusMasterResponseHandler extends ModbusResponseHandler {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(ModbusMasterResponseHandler.class);
	private short transactionIdentifierOffset = 0;

	public ModbusMasterResponseHandler() {
		this((short) 0);
	}

	public ModbusMasterResponseHandler(short transactionIdentifierOffset) {
		super();
		this.transactionIdentifierOffset = transactionIdentifierOffset;
	}

	public short getTransactionIdentifierOffset() {
		return this.transactionIdentifierOffset;
	}

	@Override
	public ModbusFrame getResponse(int transactionIdentifier) throws Exception {
		transactionIdentifier += this.transactionIdentifierOffset;
		return super.getResponse(transactionIdentifier);
	}

	@Override
	protected void processResponseFrame(Channel channel, ModbusFrame frame) {
		ModbusFrameUtil.showFrameLog(logger, channel, frame);
		/*
		 * ModbusFunction function = frame.getFunction(); if (function instanceof
		 * ReadCoilsResponse) { ReadCoilsResponse resp = (ReadCoilsResponse) function; }
		 */
	}

}
