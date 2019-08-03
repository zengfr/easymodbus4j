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
/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
public interface ModbusSlaveRequestProcessor extends ModbusProcessor {
	  

	  ReadCoilsResponse readCoils(short unitId, ReadCoilsRequest request);

	  ReadDiscreteInputsResponse readDiscreteInputs(short unitId, ReadDiscreteInputsRequest request);

	  ReadInputRegistersResponse readInputRegisters(short unitId, ReadInputRegistersRequest request);

	  ReadHoldingRegistersResponse readHoldingRegisters(short unitId, ReadHoldingRegistersRequest request);

	  WriteSingleCoilResponse writeSingleCoil(short unitId, WriteSingleCoilRequest request);

	  WriteSingleRegisterResponse writeSingleRegister(short unitId, WriteSingleRegisterRequest request);

	  WriteMultipleCoilsResponse writeMultipleCoils(short unitId, WriteMultipleCoilsRequest request);

	  WriteMultipleRegistersResponse writeMultipleRegisters(short unitId, WriteMultipleRegistersRequest request);

}
