package com.github.zengfr.easymodbus4j.example.handler;

import java.util.BitSet;
import java.util.Random;

import com.github.zengfr.easymodbus4j.func.request.ReadCoilsRequest;
import com.github.zengfr.easymodbus4j.func.request.ReadDiscreteInputsRequest;
import com.github.zengfr.easymodbus4j.func.request.ReadHoldingRegistersRequest;
import com.github.zengfr.easymodbus4j.func.request.ReadInputRegistersRequest;
import com.github.zengfr.easymodbus4j.func.request.WriteMultipleCoilsRequest;
import com.github.zengfr.easymodbus4j.func.request.WriteMultipleRegistersRequest;
import com.github.zengfr.easymodbus4j.func.request.WriteSingleCoilRequest;
import com.github.zengfr.easymodbus4j.func.request.WriteSingleRegisterRequest;
import com.github.zengfr.easymodbus4j.func.response.ReadCoilsResponse;
import com.github.zengfr.easymodbus4j.func.response.ReadDiscreteInputsResponse;
import com.github.zengfr.easymodbus4j.func.response.ReadHoldingRegistersResponse;
import com.github.zengfr.easymodbus4j.func.response.ReadInputRegistersResponse;
import com.github.zengfr.easymodbus4j.func.response.WriteMultipleCoilsResponse;
import com.github.zengfr.easymodbus4j.func.response.WriteMultipleRegistersResponse;
import com.github.zengfr.easymodbus4j.func.response.WriteSingleCoilResponse;
import com.github.zengfr.easymodbus4j.func.response.WriteSingleRegisterResponse;
import com.github.zengfr.easymodbus4j.handle.ModbusRequestHandler;
import com.github.zengfr.easymodbus4j.protocol.ModbusFrame;
import com.github.zengfr.easymodbus4j.protocol.ModbusFunction;
import com.github.zengfr.easymodbus4j.util.BitSetUtil;
import com.github.zengfr.easymodbus4j.util.ModbusFrameUtil;
import com.github.zengfr.easymodbus4j.util.RegistersUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

@ChannelHandler.Sharable
public class ModbusSlaveRequestHandler extends ModbusRequestHandler {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(ModbusSlaveRequestHandler.class);
	protected static Random random = new Random();
	private short transactionIdentifierOffset = 0;

	public ModbusSlaveRequestHandler() {
		this((short) 0);
	}

	public ModbusSlaveRequestHandler(short transactionIdentifierOffset) {
		super();
		this.transactionIdentifierOffset = transactionIdentifierOffset;
	}

	@Override
	protected int getTransactionIdentifier(int transactionIdentifier) {
		return transactionIdentifier + transactionIdentifierOffset;
	}

	@Override
	protected ModbusFunction processRequestFrame(Channel channel, ModbusFrame frame) {
		ModbusFrameUtil.showFrameLog(logger, channel, frame);
		return super.processRequestFrame(channel, frame);
	}

	@Override
	protected WriteSingleCoilResponse writeSingleCoil(WriteSingleCoilRequest request) {
		return new WriteSingleCoilResponse(request.getOutputAddress(), request.isState());
	}

	@Override
	protected WriteSingleRegisterResponse writeSingleRegister(WriteSingleRegisterRequest request) {
		return new WriteSingleRegisterResponse(request.getRegisterAddress(), request.getRegisterValue());
	}

	@Override
	protected ReadCoilsResponse readCoils(ReadCoilsRequest request) {
		BitSet coils = new BitSet(request.getQuantityOfCoils());
		coils = BitSetUtil.getRandomBits(request.getQuantityOfCoils(), random);
		if (coils.size() > 0 && random.nextInt(99) < 66)
			coils.set(0, false);
		return new ReadCoilsResponse(coils);
	}

	@Override
	protected ReadDiscreteInputsResponse readDiscreteInputs(ReadDiscreteInputsRequest request) {
		BitSet coils = new BitSet(request.getQuantityOfCoils());
		coils = BitSetUtil.getRandomBits(request.getQuantityOfCoils(), random);

		return new ReadDiscreteInputsResponse(coils);
	}

	@Override
	protected ReadInputRegistersResponse readInputRegisters(ReadInputRegistersRequest request) {
		int[] registers = new int[request.getQuantityOfInputRegisters()];
		registers = RegistersUtil.getRandomRegisters(registers.length, 1, 1024, random);

		return new ReadInputRegistersResponse(registers);
	}

	@Override
	protected ReadHoldingRegistersResponse readHoldingRegisters(ReadHoldingRegistersRequest request) {
		int[] registers = new int[request.getQuantityOfInputRegisters()];
		registers = RegistersUtil.getRandomRegisters(registers.length, 1, 1024, random);

		return new ReadHoldingRegistersResponse(registers);

	}

	@Override
	protected WriteMultipleRegistersResponse writeMultipleRegisters(WriteMultipleRegistersRequest request) {
		return new WriteMultipleRegistersResponse(request.getStartingAddress(), request.getQuantityOfRegisters());
	}

	@Override
	protected WriteMultipleCoilsResponse writeMultipleCoils(WriteMultipleCoilsRequest request) {
		return new WriteMultipleCoilsResponse(request.getStartingAddress(), request.getQuantityOfOutputs());
	}

}
