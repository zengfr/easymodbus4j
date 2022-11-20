package com.github.zengfr.easymodbus4j.app.plugin.impl;

import com.github.zengfr.easymodbus4j.app.plugin.DeviceCommandPlugin;
import com.github.zengfr.easymodbus4j.app.plugin.DeviceRepositoryPlugin;
import com.github.zengfr.easymodbus4j.app.plugin.DeviceRepositoryPluginRegister;
import com.github.zengfr.easymodbus4j.codec.tcp.ModbusTcpDecoder;
import com.github.zengfr.easymodbus4j.protocol.tcp.ModbusFrame;
import com.github.zengfr.easymodbus4j.util.ModbusTransactionIdUtil;

import io.netty.buffer.ByteBuf;

public abstract class DeviceCommandAbstractPlugin implements DeviceCommandPlugin  {
	protected int calculateTransactionIdentifier() {
		return ModbusTransactionIdUtil.calculateTransactionId();
	}

	protected DeviceRepositoryPlugin getRepositoryPlugin() {
		return DeviceRepositoryPluginRegister.getInstance().get();
	}

	protected ModbusFrame byteBuf2Frame(ByteBuf buffer, boolean decodeRequest) {
		return ModbusTcpDecoder.decodeFrame(buffer, decodeRequest);
	}

	protected ByteBuf frame2ByteBuf(ModbusFrame frame) {
		return frame.encode();
	}

}
