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
package com.github.zengfr.easymodbus4j.handle.impl;

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
import com.github.zengfr.easymodbus4j.processor.ModbusSlaveRequestProcessor;
import com.github.zengfr.easymodbus4j.protocol.ModbusFunction;
import com.github.zengfr.easymodbus4j.protocol.tcp.ModbusFrame;
import com.github.zengfr.easymodbus4j.util.ModbusFrameUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
@ChannelHandler.Sharable
public class ModbusSlaveRequestHandler extends ModbusRequestHandler {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(ModbusSlaveRequestHandler.class);

	private ModbusSlaveRequestProcessor processor;

	public short getTransactionIdentifierOffset() {
		return this.processor.getTransactionIdentifierOffset();
	}

	public ModbusSlaveRequestHandler(ModbusSlaveRequestProcessor processor) {

		this.processor = processor;
	}

	@Override
	protected int getRespTransactionIdByReqTransactionId(int reqTransactionIdentifier) {
		return reqTransactionIdentifier + this.getTransactionIdentifierOffset();
	}

	@Override
	protected ModbusFunction processRequestFrame(Channel channel, ModbusFrame frame) {
		if (this.processor.isShowFrameDetail()) {
			ModbusFrameUtil.showFrameLog(logger, channel, frame, true);
		}
		return super.processRequestFrame(channel, frame);
	}

	@Override
	protected WriteSingleCoilResponse writeSingleCoil(short unitIdentifier, WriteSingleCoilRequest request) {

		return this.processor.writeSingleCoil(unitIdentifier, request);
	}

	@Override
	protected WriteSingleRegisterResponse writeSingleRegister(short unitIdentifier, WriteSingleRegisterRequest request) {
		return this.processor.writeSingleRegister(unitIdentifier, request);
	}

	@Override
	protected ReadCoilsResponse readCoils(short unitIdentifier, ReadCoilsRequest request) {

		return this.processor.readCoils(unitIdentifier, request);
	}

	@Override
	protected ReadDiscreteInputsResponse readDiscreteInputs(short unitIdentifier, ReadDiscreteInputsRequest request) {

		return this.processor.readDiscreteInputs(unitIdentifier, request);
	}

	@Override
	protected ReadInputRegistersResponse readInputRegisters(short unitIdentifier, ReadInputRegistersRequest request) {

		return this.processor.readInputRegisters(unitIdentifier, request);
	}

	@Override
	protected ReadHoldingRegistersResponse readHoldingRegisters(short unitIdentifier, ReadHoldingRegistersRequest request) {

		return this.processor.readHoldingRegisters(unitIdentifier, request);

	}

	@Override
	protected WriteMultipleCoilsResponse writeMultipleCoils(short unitIdentifier, WriteMultipleCoilsRequest request) {
		return this.processor.writeMultipleCoils(unitIdentifier, request);
	}

	@Override
	protected WriteMultipleRegistersResponse writeMultipleRegisters(short unitIdentifier, WriteMultipleRegistersRequest request) {

		return this.processor.writeMultipleRegisters(unitIdentifier, request);
	}

}
