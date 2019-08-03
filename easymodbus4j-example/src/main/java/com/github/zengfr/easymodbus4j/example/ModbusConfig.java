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
package com.github.zengfr.easymodbus4j.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.github.zengfr.easymodbus4j.ModbusConsts;
import com.github.zengfr.easymodbus4j.common.util.ParseStringUtil;
import com.github.zengfr.easymodbus4j.ModbusConfs;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
public class ModbusConfig {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(ModbusConfig.class);
	public int type;
	public String host;
	public int port;
	public boolean showDebugLog;
	public boolean autoSend;
	public int idleTimeOut;
	public int sleep;

	public short unit_IDENTIFIER;
	public short transactionIdentifierOffset;

	public int udpPort;
	public String heartbeat;

	public static ModbusConfig parse(String[] argsArray) {
		ModbusConfig cfg = new ModbusConfig();

		cfg.type = ParseStringUtil.parseInt(argsArray, 0, 0);
		cfg.host = ParseStringUtil.parseString(argsArray, 1, ModbusConsts.DEFAULT_HOTST_IP);
		cfg.port = ParseStringUtil.parseInt(argsArray, 2, ModbusConfs.DEFAULT_MODBUS_PORT);
		cfg.unit_IDENTIFIER = ParseStringUtil.parseShort(argsArray, 3, ModbusConsts.DEFAULT_UNIT_IDENTIFIER);
		cfg.transactionIdentifierOffset = ParseStringUtil.parseShort(argsArray, 4, (short) 0);
		cfg.showDebugLog = ParseStringUtil.parseBoolean(argsArray, 5, true);
		cfg.idleTimeOut=ParseStringUtil.parseInt(argsArray, 6, ModbusConfs.IDLE_TIMEOUT_SECOND);
		
		cfg.autoSend = ParseStringUtil.parseBoolean(argsArray, 7, true);
		cfg.sleep = ParseStringUtil.parseInt(argsArray, 8, 1000 * 15);
		cfg.heartbeat = ParseStringUtil.parseString(argsArray, 9, ModbusConsts.HEARTBEAT);
		
		cfg.udpPort = ParseStringUtil.parseInt(argsArray, 10, ModbusConfs.DEFAULT_MODBUS_PORT5);
		
		logger.info(JSON.toJSONString(cfg));
		return cfg;
	}
}
