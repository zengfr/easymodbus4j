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

import com.github.zengfr.easymodbus4j.func.AbstractRequest;
import com.github.zengfr.easymodbus4j.handler.ModbusResponseHandler;
import com.github.zengfr.easymodbus4j.logging.ChannelLogger;
import com.github.zengfr.easymodbus4j.processor.ModbusMasterResponseProcessor;
import com.github.zengfr.easymodbus4j.protocol.ModbusFunction;
import com.github.zengfr.easymodbus4j.protocol.tcp.ModbusFrame;
import com.github.zengfr.easymodbus4j.util.ModbusFrameUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
@ChannelHandler.Sharable
public class ModbusMasterResponseHandler extends ModbusResponseHandler {
	private static final ChannelLogger logger = ChannelLogger.getLogger(ModbusMasterResponseHandler.class);
	private ModbusMasterResponseProcessor processor;

	public short getTransactionIdentifierOffset() {
		return this.processor.getTransactionIdentifierOffset();
	}
	public ModbusMasterResponseHandler(ModbusMasterResponseProcessor processor) {
		super(true);
		this.processor = processor;
	}

	@Override
	protected boolean processResponseFrame(Channel channel, ModbusFrame frame) {
		if (this.processor.isShowFrameDetail()) {
			ModbusFrameUtil.showFrameLog(logger, channel, frame, true);
		}
		return super.processResponseFrame(channel, frame);
	}

	@Override
	protected int getReqTransactionIdByRespTransactionId(int respTransactionIdentifierOffset) {
		return respTransactionIdentifierOffset - this.getTransactionIdentifierOffset();
	}

	protected int getRespTransactionIdByReqTransactionId(int reqTransactionIdentifier) {
		return reqTransactionIdentifier + this.getTransactionIdentifierOffset();
	}

	@Override
	public ModbusFrame getResponseCache(int reqTransactionIdentifier,short funcCode) throws Exception {
		int respTransactionIdentifier = getRespTransactionIdByReqTransactionId(reqTransactionIdentifier);
		return super.getResponseCache(respTransactionIdentifier,  funcCode);
	}

	@Override
	protected boolean processResponseFrame(Channel channel, int unitId, AbstractRequest reqFunc, ModbusFunction respFunc) {
		return this.processor.processResponseFrame(channel, unitId, reqFunc, respFunc);
		
	}

}
