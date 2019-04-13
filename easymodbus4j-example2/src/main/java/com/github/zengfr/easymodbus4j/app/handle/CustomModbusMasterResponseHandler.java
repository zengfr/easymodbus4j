package com.github.zengfr.easymodbus4j.app.handle;

import java.lang.reflect.Field;

import com.github.zengfr.easymodbus4j.app.plugin.DeviceRepositoryPlugin;
import com.github.zengfr.easymodbus4j.app.plugin.DeviceRepositoryPluginRegister;
import com.github.zengfr.easymodbus4j.cache.ModebusFrameCacheFactory;
import com.github.zengfr.easymodbus4j.example.handler.ModbusMasterResponseHandler;
import com.github.zengfr.easymodbus4j.func.AbstractFunction;
import com.github.zengfr.easymodbus4j.protocol.ModbusFrame;
import com.github.zengfr.easymodbus4j.protocol.ModbusFunction;
import com.github.zengfr.easymodbus4j.util.ByteUtil;
import com.github.zengfr.easymodbus4j.util.HexUtil;
import com.github.zengfr.easymodbus4j.util.ModbusFunctionUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

@ChannelHandler.Sharable
public class CustomModbusMasterResponseHandler extends ModbusMasterResponseHandler {
	private static final InternalLogger logger = InternalLoggerFactory
			.getInstance(CustomModbusMasterResponseHandler.class.getSimpleName());

	public CustomModbusMasterResponseHandler() {
		this((short) 0);
	}

	public CustomModbusMasterResponseHandler(short transactionIdentifierOffset) {
		super(transactionIdentifierOffset);
	}

	@Override
	protected void processResponseFrame(Channel channel, ModbusFrame respFrame) {
		super.processResponseFrame(channel, respFrame);
		int respTransactionIdentifier = respFrame.getHeader().getTransactionIdentifier();
		ModbusFrame reqFrame = ModebusFrameCacheFactory.getInstance().getRequestCache()
				.get(respTransactionIdentifier - this.getTransactionIdentifierOffset());
		boolean isErr = false;
		if (reqFrame != null) {
			if (reqFrame.getFunction() instanceof AbstractFunction) {
				AbstractFunction reqFunc = (AbstractFunction) reqFrame.getFunction();
				ModbusFunction respFunc = respFrame.getFunction();
				processResponseFrame(channel, reqFunc, respFunc);
			} else {
				isErr = true;
			}
		} else {
			isErr = true;
		}
		if (isErr) {
			logger.error(String.format("req is null:%s;%s", channel.remoteAddress(), respFrame));
		}
	}

	public void processResponseFrame(Channel channel, AbstractFunction reqFunc, ModbusFunction respFunc) {

		if (reqFunc != null) {
			short funCode = reqFunc.getFunctionCode();
			int address;
			int quantityOfInputRegisters;
			try {
				Field addressF = AbstractFunction.class.getDeclaredField("address");
				Field valueF = AbstractFunction.class.getDeclaredField("value");
				addressF.setAccessible(true);
				valueF.setAccessible(true);

				address = addressF.getInt(reqFunc);
				quantityOfInputRegisters = valueF.getInt(reqFunc);
				byte[] valuesArray = ModbusFunctionUtil.getFunctionValues(respFunc);
				processResponse(channel, funCode, address, quantityOfInputRegisters, valuesArray);
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				logger.error(e);
			}
		}
	}

	protected void processResponse(Channel channel, short funCode, int address, int quantityOfInputRegisters,
			byte[] valuesArray) {
		logger.info(String.format("processRespFrame:%s;%s;%s;%s;%s", channel.remoteAddress(), funCode, address,
				quantityOfInputRegisters, HexUtil.bytesToHexString(valuesArray, " ")));
		String ipAndPort = String.format("%s", channel.remoteAddress().toString().replaceAll("/", ""));
		int[] value = ByteUtil.toIntArray(valuesArray);
		String valueHexStr = HexUtil.bytesToHexString(valuesArray, " ");
		DeviceRepositoryPlugin repositoryPlugin = getDeviceRepositoryPlugin();
		if (repositoryPlugin.isGetDeviceIdReq(funCode, address, quantityOfInputRegisters)) {
			if (value.length > 0) {
				String deviceId = "" + value[0];
				repositoryPlugin.updateDeviceIpAndPort(deviceId, ipAndPort);
			}
		}
		String deviceId = repositoryPlugin.getDeviceIdByIpAndPort(ipAndPort);
		repositoryPlugin.updateFuctionValue(deviceId, funCode, address, valueHexStr);
	}

	protected DeviceRepositoryPlugin getDeviceRepositoryPlugin() {
		return DeviceRepositoryPluginRegister.getInstance().get();
	}
}
