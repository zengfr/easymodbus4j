package com.github.zengfr.easymodbus4j.app.plugin.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import com.github.zengfr.easymodbus4j.app.common.DeviceCommand;
import com.github.zengfr.easymodbus4j.app.common.FunctionCode;
import com.github.zengfr.easymodbus4j.app.plugin.DeviceCommandPlugin;
import com.github.zengfr.easymodbus4j.func.request.ReadCoilsRequest;
import com.github.zengfr.easymodbus4j.func.request.ReadDiscreteInputsRequest;
import com.github.zengfr.easymodbus4j.func.request.ReadHoldingRegistersRequest;
import com.github.zengfr.easymodbus4j.func.request.ReadInputRegistersRequest;
import com.github.zengfr.easymodbus4j.func.request.WriteMultipleCoilsRequest;
import com.github.zengfr.easymodbus4j.func.request.WriteMultipleRegistersRequest;
import com.github.zengfr.easymodbus4j.func.request.WriteSingleCoilRequest;
import com.github.zengfr.easymodbus4j.func.request.WriteSingleRegisterRequest;
import com.github.zengfr.easymodbus4j.protocol.ModbusFrame;
import com.github.zengfr.easymodbus4j.protocol.ModbusFunction;
import com.github.zengfr.easymodbus4j.protocol.ModbusHeader;
import com.google.common.primitives.Booleans;
import io.netty.buffer.ByteBuf;

public class DeviceCommandV1PluginImpl extends DeviceCommandAbstractPlugin implements DeviceCommandPlugin {

	public DeviceCommandV1PluginImpl() {
	}

	@Override
	public <T> ByteBuf buildRequestFrame(DeviceCommand<T> cmd) {
		ModbusFunction function = null;
		int fun = cmd.functionCode;
		int address = cmd.address;
		String v1 = "" + cmd.value;
		String v2 = StringUtils.join(cmd.values, ",");
		switch (fun) {
		case FunctionCode.WRITE_SINGLE_COIL:
			boolean state = Boolean.valueOf(v1);
			function = new WriteSingleCoilRequest(address, state);
			break;
		case FunctionCode.WRITE_SINGLE_REGISTER:
			int value = Integer.valueOf(v1);
			function = new WriteSingleRegisterRequest(address, value);
			break;
		case FunctionCode.READ_COILS:
			int quantityOfCoils = Integer.valueOf(v1);
			function = new ReadCoilsRequest(address, quantityOfCoils);
			break;
		case FunctionCode.READ_DISCRETE_INPUTS:
			int quantityOfCoils2 = Integer.valueOf(v1);
			function = new ReadDiscreteInputsRequest(address, quantityOfCoils2);
			break;
		case FunctionCode.READ_INPUT_REGISTERS:
			int quantityOfInputRegisters = Integer.valueOf(v1);
			function = new ReadInputRegistersRequest(address, quantityOfInputRegisters);
			break;
		case FunctionCode.READ_HOLDING_REGISTERS:
			int quantityOfInputRegisters2 = Integer.valueOf(v1);
			function = new ReadHoldingRegistersRequest(address, quantityOfInputRegisters2);
			break;
		case FunctionCode.WRITE_MULTIPLE_REGISTERS:
			String[] registersArray = StringUtils.split(v2, ",");
			int[] registers = Arrays.stream(registersArray).mapToInt(Integer::valueOf).toArray();
			int quantityOfRegisters = registers.length;
			function = new WriteMultipleRegistersRequest(address, quantityOfRegisters, registers);
			break;
		case FunctionCode.WRITE_MULTIPLE_COILS:
			String[] registersArray2 = StringUtils.split(v2, ",");
			List<Boolean> outputsValueList = Arrays.stream(registersArray2).map(DeviceCommandV1PluginImpl::parseToBool)
					.collect(Collectors.toList());
			boolean[] outputsValue = Booleans.toArray(outputsValueList);
			int quantityOfOutputs = outputsValue.length;
			function = new WriteMultipleCoilsRequest(address, quantityOfOutputs, outputsValue);
			break;
		default:
			break;
		}
		int transactionId = calculateTransactionIdentifier();
		int pduLength = function.calculateLength();
		int protocolIdentifier = 0;
		short unitIdentifier = 1;
		ModbusHeader header = new ModbusHeader(transactionId, protocolIdentifier, pduLength, unitIdentifier);
		ModbusFrame frame = new ModbusFrame(header, function);
		ByteBuf buffer = frame2ByteBuf(frame);
		return buffer;
	}

	private static boolean parseToBool(String v) {
		return v != null && (v.equalsIgnoreCase("1") || v.equalsIgnoreCase("T"));
	}

	@Override
	public void parseResponseFrame(ByteBuf buffer) {
		// ModbusFrame frame = byteBuf2Frame(buffer, false);
		// DeviceRepositoryPlugin deviceRepositoryPlugin = getRepositoryPlugin();
		// deviceRepositoryPlugin.getFunctionValues(version, cmd, values);
		// deviceRepositoryPlugin.updateCommandValue(cmd, value);
	}

	@Override
	public boolean isEnabled(DeviceCommand cmd) {
		return true;
	}
}
