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
package com.github.zengfr.easymodbus4j.processor;

import com.github.zengfr.easymodbus4j.func.AbstractRequest;
import com.github.zengfr.easymodbus4j.protocol.ModbusFunction;
import com.github.zengfr.easymodbus4j.util.ModbusFunctionUtil;

/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
public abstract class AbstractModbusProcessor implements ModbusProcessor {
	private short transactionIdentifierOffset;
	private boolean isShowFrameDetail;

	public AbstractModbusProcessor() {
		this((short) 0, true);
	}

	public AbstractModbusProcessor(short transactionIdentifierOffset, boolean isShowFrameDetail) {
		this.transactionIdentifierOffset = transactionIdentifierOffset;
		this.isShowFrameDetail = isShowFrameDetail;

	}

	protected boolean isRequestResponseMatch(AbstractRequest reqFunc, ModbusFunction respFunc) {
		return reqFunc != null && respFunc != null && reqFunc.getFunctionCode() == respFunc.getFunctionCode();
	}

	protected boolean isRequestResponseValueMatch(AbstractRequest reqFunc, ModbusFunction respFunc) {
		byte[] respFuncValuesArray = ModbusFunctionUtil.getFunctionValues(respFunc);
		return isRequestResponseValueMatch(reqFunc, respFuncValuesArray);
	}

	protected boolean isRequestResponseValueMatch(AbstractRequest reqFunc, byte[] respFuncValuesArray) {
		if (reqFunc == null)
			return false;
		int quantityOfInputRegisters = reqFunc.getValue();
		return (quantityOfInputRegisters * 2 == respFuncValuesArray.length) || (respFuncValuesArray.length == 1 && quantityOfInputRegisters == respFuncValuesArray.length);
	}

	public short getTransactionIdentifierOffset() {
		return this.transactionIdentifierOffset;
	}

	public boolean isShowFrameDetail() {
		return isShowFrameDetail;
	};

}
