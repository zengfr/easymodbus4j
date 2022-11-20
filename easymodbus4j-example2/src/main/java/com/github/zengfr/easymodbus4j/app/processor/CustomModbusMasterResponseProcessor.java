package com.github.zengfr.easymodbus4j.app.processor;

import com.github.zengfr.easymodbus4j.app.plugin.DeviceRepositoryPlugin;
import com.github.zengfr.easymodbus4j.app.plugin.DeviceRepositoryPluginRegister;
import com.github.zengfr.easymodbus4j.common.util.ByteUtil;
import com.github.zengfr.easymodbus4j.common.util.HexUtil;
import com.github.zengfr.easymodbus4j.func.AbstractRequest;
import com.github.zengfr.easymodbus4j.processor.AbstractModbusProcessor;
import com.github.zengfr.easymodbus4j.processor.ModbusMasterResponseProcessor;
import com.github.zengfr.easymodbus4j.protocol.ModbusFunction;
import com.github.zengfr.easymodbus4j.util.ModbusFunctionUtil;

import io.netty.channel.Channel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class CustomModbusMasterResponseProcessor extends AbstractModbusProcessor implements ModbusMasterResponseProcessor {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(CustomModbusMasterResponseProcessor.class.getSimpleName());

	public CustomModbusMasterResponseProcessor(short transactionIdentifierOffset) {
		super(transactionIdentifierOffset, true);
	}

	@Override
	public boolean processResponseFrame(Channel channel, int unitId, AbstractRequest reqFunc, ModbusFunction respFunc) {
		boolean success = false;
		boolean isMatch = this.isRequestResponseMatch(reqFunc, respFunc);
		if (isMatch) {
			try {
				byte[] respFuncValuesArray = ModbusFunctionUtil.getFunctionValues(respFunc);
				success = isRequestResponseValueMatch(reqFunc, respFuncValuesArray);
				logger.info(String.format("processResponseFrame:%s;%s;%s;%s;%s;%s", success, channel.remoteAddress(), unitId, HexUtil.bytesToHexString(respFuncValuesArray), reqFunc, respFunc));
				short funCode = reqFunc.getFunctionCode();
				int address = reqFunc.getAddress();
				int quantityOfInputRegisters = reqFunc.getValue();

				processResponse(channel, funCode, address, quantityOfInputRegisters, respFuncValuesArray);
			} catch (IllegalArgumentException | SecurityException e) {
				logger.error(e);
			}
		}
		if (!success) {
			logger.warn(String.format("isMatch:%s;success:%s;%s;%s;%s;%s", isMatch, success, channel.remoteAddress(), unitId, reqFunc, respFunc));
		}
		return success;
	}

	protected void processResponse(Channel channel, short funCode, int address, int quantityOfInputRegisters, byte[] valuesArray) {
		logger.debug(String.format("processResponse:%s;%s;%s;%s;%s", channel.remoteAddress(), funCode, address, quantityOfInputRegisters, HexUtil.bytesToHexString(valuesArray, " ")));
		String ipAndPort = String.format("%s", channel.remoteAddress().toString().replaceAll("/", ""));
		int[] value = ByteUtil.toIntArray(valuesArray);
		String valueHexStr = HexUtil.bytesToHexString(valuesArray, " ");
		DeviceRepositoryPlugin repositoryPlugin = getDeviceRepositoryPlugin();
		if (repositoryPlugin.isGetDeviceIdReq(funCode, address, quantityOfInputRegisters)) {
			if (value.length > 0 && value[0] > 0) {
				String deviceId = "" + value[0];
				repositoryPlugin.updateDeviceIpAndPort(deviceId, ipAndPort);
			}
		} else {
			String deviceId = repositoryPlugin.getDeviceIdByIpAndPort(ipAndPort);
			repositoryPlugin.updateFuctionValue(ipAndPort, deviceId, funCode, address, valueHexStr);
		}
	}

	protected DeviceRepositoryPlugin getDeviceRepositoryPlugin() {
		return DeviceRepositoryPluginRegister.getInstance().get();
	}

}
