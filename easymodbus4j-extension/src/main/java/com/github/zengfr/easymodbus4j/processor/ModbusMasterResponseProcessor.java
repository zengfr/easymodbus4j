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

import com.github.zengfr.easymodbus4j.func.AbstractFunction;
import com.github.zengfr.easymodbus4j.protocol.ModbusFunction;

import io.netty.channel.Channel;
/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
public interface ModbusMasterResponseProcessor extends ModbusProcessor {
	 
	void processResponseFrame(Channel channel,int unitId, AbstractFunction reqFunc, ModbusFunction respFunc);
}
