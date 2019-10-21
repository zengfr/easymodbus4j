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
 */package com.github.zengfr.easymodbus4j.example.processor;

import com.github.zengfr.easymodbus4j.func.AbstractRequest;
import com.github.zengfr.easymodbus4j.func.request.ReadCoilsRequest;
import com.github.zengfr.easymodbus4j.func.request.ReadDiscreteInputsRequest;
import com.github.zengfr.easymodbus4j.func.response.ReadCoilsResponse;
import com.github.zengfr.easymodbus4j.func.response.ReadDiscreteInputsResponse;
import com.github.zengfr.easymodbus4j.handle.impl.ModbusMasterResponseHandler;
import com.github.zengfr.easymodbus4j.processor.AbstractModbusProcessor;
import com.github.zengfr.easymodbus4j.processor.ModbusMasterResponseProcessor;
import com.github.zengfr.easymodbus4j.protocol.ModbusFunction;
import com.github.zengfr.easymodbus4j.util.ModbusFunctionUtil;

import io.netty.channel.Channel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
public class ExampleModbusMasterResponseProcessor extends AbstractModbusProcessor implements ModbusMasterResponseProcessor {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(ExampleModbusMasterResponseProcessor.class);
	public ExampleModbusMasterResponseProcessor(short transactionIdentifierOffset) {
		super(transactionIdentifierOffset, true);
	}

	public boolean processResponseFrame(Channel channel, int unitId, AbstractRequest reqFunc, ModbusFunction respFunc) {
        boolean success=this.isRequestResponseMatch(reqFunc, respFunc);
		if (respFunc instanceof ReadCoilsResponse) {
			ReadCoilsResponse resp = (ReadCoilsResponse) respFunc;
			if (reqFunc instanceof ReadCoilsRequest) {
				ReadCoilsRequest req = (ReadCoilsRequest) reqFunc;
				// process business logic
				success=true;
			}
		}
		if (respFunc instanceof ReadDiscreteInputsResponse) {
			ReadDiscreteInputsResponse resp = (ReadDiscreteInputsResponse) respFunc;
			byte[] resutArray = resp.getInputStatus().toByteArray();
			byte[] valuesArray = ModbusFunctionUtil.getFunctionValues(respFunc);
			if (reqFunc instanceof ReadDiscreteInputsRequest) {
				ReadDiscreteInputsRequest req = (ReadDiscreteInputsRequest) reqFunc;
				// process business logic
				success=true;
			}
		}
		logger.debug(String.format("success:%s", success));
		return success;
	};
}
