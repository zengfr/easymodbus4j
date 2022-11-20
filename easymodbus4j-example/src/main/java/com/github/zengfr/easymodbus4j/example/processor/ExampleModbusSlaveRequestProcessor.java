/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.zengfr.easymodbus4j.example.processor;

import java.util.BitSet;
import java.util.Random;

import com.github.zengfr.easymodbus4j.common.util.BitSetUtil;
import com.github.zengfr.easymodbus4j.common.util.RegistersUtil;
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
import com.github.zengfr.easymodbus4j.processor.AbstractModbusProcessor;
import com.github.zengfr.easymodbus4j.processor.ModbusSlaveRequestProcessor;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
public class ExampleModbusSlaveRequestProcessor extends AbstractModbusProcessor implements ModbusSlaveRequestProcessor {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(ExampleModbusSlaveRequestProcessor.class);
	protected static Random random = new Random();

	public ExampleModbusSlaveRequestProcessor(short transactionIdentifierOffset) {
		super(transactionIdentifierOffset, true);
	}

	@Override
	public WriteSingleCoilResponse writeSingleCoil(short unitIdentifier, WriteSingleCoilRequest request) {

		return new WriteSingleCoilResponse(request.getOutputAddress(), request.isState());
	}

	@Override
	public WriteSingleRegisterResponse writeSingleRegister(short unitIdentifier, WriteSingleRegisterRequest request) {
		return new WriteSingleRegisterResponse(request.getRegisterAddress(), request.getRegisterValue());
	}

	@Override
	public ReadCoilsResponse readCoils(short unitIdentifier, ReadCoilsRequest request) {
		BitSet coils = new BitSet(request.getQuantityOfCoils());
		coils = BitSetUtil.getRandomBits(request.getQuantityOfCoils(), random);
		if (coils.size() > 0 && random.nextInt(99) < 66)
			coils.set(0, false);
		return new ReadCoilsResponse(coils);
	}

	@Override
	public ReadDiscreteInputsResponse readDiscreteInputs(short unitIdentifier, ReadDiscreteInputsRequest request) {
		BitSet coils = new BitSet(request.getQuantityOfCoils());
		coils = BitSetUtil.getRandomBits(request.getQuantityOfCoils(), random);
		//coils.set(0,79,false);
		//coils.set(0,true);
		return new ReadDiscreteInputsResponse(coils);
	}

	@Override
	public ReadInputRegistersResponse readInputRegisters(short unitIdentifier, ReadInputRegistersRequest request) {
		int[] registers = new int[request.getQuantityOfInputRegisters()];
		registers = RegistersUtil.getRandomRegisters(registers.length, 1, 1024, random);

		return new ReadInputRegistersResponse(registers);
	}

	@Override
	public ReadHoldingRegistersResponse readHoldingRegisters(short unitIdentifier, ReadHoldingRegistersRequest request) {
		int[] registers = new int[request.getQuantityOfInputRegisters()];
		registers = RegistersUtil.getRandomRegisters(registers.length, 1, 1024, random);
		// RegistersFactory.getInstance().getOrInit(unitIdentifier).getHoldingRegister(request.getStartingAddress());
		return new ReadHoldingRegistersResponse(registers);

	}

	@Override
	public WriteMultipleCoilsResponse writeMultipleCoils(short unitIdentifier, WriteMultipleCoilsRequest request) {
		return new WriteMultipleCoilsResponse(request.getStartingAddress(), request.getQuantityOfOutputs());
	}

	@Override
	public WriteMultipleRegistersResponse writeMultipleRegisters(short unitIdentifier, WriteMultipleRegistersRequest request) {
		// RegistersFactory.getInstance().getOrInit(unitIdentifier).setHoldingRegister(request.getStartingAddress(),
		// request.getRegisters());
		return new WriteMultipleRegistersResponse(request.getStartingAddress(), request.getQuantityOfRegisters());
	}

}
