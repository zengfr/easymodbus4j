package com.github.zengfr.easymodbus4j.app.plugin.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.google.common.collect.Lists;
import com.google.common.primitives.Booleans;
import org.apache.commons.lang3.StringUtils;
import io.netty.buffer.ByteBuf;

import com.github.zengfr.easymodbus4j.ModbusConsts;
import com.github.zengfr.easymodbus4j.app.common.DeviceCommand;
import com.github.zengfr.easymodbus4j.app.common.FunctionCode;
import com.github.zengfr.easymodbus4j.app.plugin.DeviceCommandPlugin;
import com.github.zengfr.easymodbus4j.common.RegisterOrder;
import com.github.zengfr.easymodbus4j.common.util.IntArrayUtil;
import com.github.zengfr.easymodbus4j.common.util.RegistersUtil;
import com.github.zengfr.easymodbus4j.func.request.ReadCoilsRequest;
import com.github.zengfr.easymodbus4j.func.request.ReadDiscreteInputsRequest;
import com.github.zengfr.easymodbus4j.func.request.ReadHoldingRegistersRequest;
import com.github.zengfr.easymodbus4j.func.request.ReadInputRegistersRequest;
import com.github.zengfr.easymodbus4j.func.request.WriteMultipleCoilsRequest;
import com.github.zengfr.easymodbus4j.func.request.WriteMultipleRegistersRequest;
import com.github.zengfr.easymodbus4j.func.request.WriteSingleCoilRequest;
import com.github.zengfr.easymodbus4j.func.request.WriteSingleRegisterRequest;
import com.github.zengfr.easymodbus4j.protocol.ModbusFunction;
import com.github.zengfr.easymodbus4j.protocol.tcp.ModbusFrame;
import com.github.zengfr.easymodbus4j.protocol.tcp.ModbusHeader;

public class DeviceCommandV1PluginImpl extends DeviceCommandAbstractPlugin implements DeviceCommandPlugin {

	public DeviceCommandV1PluginImpl() {
	}

	@Override
	public <T> ByteBuf buildRequestFrame(DeviceCommand<T> cmd) {
		ModbusFunction function = null;
		int fun = cmd.getFunctionCode();
		int address = cmd.getAddress();
		String v1 = "" + cmd.getValue();
		String v2 = StringUtils.join(cmd.getValues(), ",");
		String v3 = StringUtils.strip(v1 + "," + v2, ",");
		List<String> vList = Lists.newArrayList(v3.split(","));

		if (!vList.isEmpty()) {
			String v = vList.get(0);
			switch (fun) {
			case FunctionCode.WRITE_SINGLE_COIL:
				boolean state = Boolean.valueOf(v);
				function = new WriteSingleCoilRequest(address, state);
				break;
			case FunctionCode.WRITE_SINGLE_REGISTER:
				int value = Integer.valueOf(v);
				function = new WriteSingleRegisterRequest(address, value);
				break;
			case FunctionCode.READ_COILS:
				int quantityOfCoils = Integer.valueOf(v);
				function = new ReadCoilsRequest(address, quantityOfCoils);
				break;
			case FunctionCode.READ_DISCRETE_INPUTS:
				int quantityOfCoils2 = Integer.valueOf(v);
				function = new ReadDiscreteInputsRequest(address, quantityOfCoils2);
				break;
			case FunctionCode.READ_INPUT_REGISTERS:
				int quantityOfInputRegisters = Integer.valueOf(v);
				function = new ReadInputRegistersRequest(address, quantityOfInputRegisters);
				break;
			case FunctionCode.READ_HOLDING_REGISTERS:
				int quantityOfInputRegisters2 = Integer.valueOf(v);
				function = new ReadHoldingRegistersRequest(address, quantityOfInputRegisters2);
				break;
			case FunctionCode.WRITE_MULTIPLE_REGISTERS:
				int[] registers = covertToIntegerArray("" + cmd.getValueType(), vList);
				int quantityOfRegisters = registers.length;
				function = new WriteMultipleRegistersRequest(address, quantityOfRegisters, registers);
				break;
			case FunctionCode.WRITE_MULTIPLE_COILS:
				String[] registersArray2 = StringUtils.split(v3, ",");
				List<Boolean> outputsValueList = Arrays.stream(registersArray2)
						.map(DeviceCommandV1PluginImpl::parseToBool).collect(Collectors.toList());
				boolean[] outputsValue = Booleans.toArray(outputsValueList);
				int quantityOfOutputs = outputsValue.length;
				function = new WriteMultipleCoilsRequest(address, quantityOfOutputs, outputsValue);
				break;
			default:
				break;
			}
		}
		int transactionId = calculateTransactionIdentifier();
		int pduLength = function.calculateLength();
		int protocolIdentifier = ModbusConsts.DEFAULT_PROTOCOL_IDENTIFIER;
		short unitIdentifier = ModbusConsts.DEFAULT_UNIT_IDENTIFIER;
		ModbusHeader header = new ModbusHeader(transactionId, protocolIdentifier, pduLength, unitIdentifier);
		ModbusFrame frame = new ModbusFrame(header, function);
		ByteBuf buffer = frame2ByteBuf(frame);
		return buffer;
	}

	public static int[] covertToIntegerArray(String valueType, Iterable<String> vArray) {
		ArrayList<Integer> list = Lists.newArrayList();
		for (String vItem : vArray) {
			if (vItem != null && !vItem.isEmpty() && !vItem.equals("null")) {
				int[] vv = covertToIntegerArray(valueType, vItem);
				for (Integer v : vv) {
					list.add(v);
				}
			}
		}
		return IntArrayUtil.toIntArray(list);
	}

	private static int[] covertToIntegerArray(String valueType, String v) {
		switch (valueType.toLowerCase()) {
		case "integer":
			Integer i = Integer.valueOf(v);
			return RegistersUtil.convertIntToRegisters(i, RegisterOrder.HighLow);
		case "float":
			Float f = Float.valueOf(v);
			return RegistersUtil.convertFloatToRegisters(f, RegisterOrder.HighLow);
		case "double":
			Double d = Double.valueOf(v);
			return RegistersUtil.convertDoubleToRegisters(d, RegisterOrder.HighLow);
		case "long":
			Long l = Long.valueOf(v);
			return RegistersUtil.convertLongToRegisters(l, RegisterOrder.HighLow);
		case "string":
			return RegistersUtil.convertStringToRegisters(v);
		}
		return new int[] { Integer.valueOf(v) };
	}

	private static boolean parseToBool(String v) {
		return v != null && (v.equalsIgnoreCase("1") || v.equalsIgnoreCase("T"));
	}

	//@Override
	//public void parseResponseFrame(ByteBuf buffer) {
		// ModbusFrame frame = byteBuf2Frame(buffer, false);
		// DeviceRepositoryPlugin deviceRepositoryPlugin = getRepositoryPlugin();
		// deviceRepositoryPlugin.getFunctionValues(version, cmd, values);
		// deviceRepositoryPlugin.updateCommandValue(cmd, value);
	//}

	@Override
	public <T> boolean isEnabled(DeviceCommand<T> cmd) {
		return true;
	}
}
