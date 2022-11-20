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
package com.github.zengfr.easymodbus4j.example.schedule;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.zengfr.easymodbus4j.common.util.FileUtil;
import com.github.zengfr.easymodbus4j.common.util.InputStreamUtil;
import com.github.zengfr.easymodbus4j.schedule.ModbusMasterSchedule;
import com.github.zengfr.easymodbus4j.sender.util.ModbusRequestSendUtil.PriorityStrategy;
import com.google.common.collect.Lists;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
public class ModbusMasterSchedule4ConfigFile extends ModbusMasterSchedule {
	private static Logger logger = LoggerFactory
			.getLogger(ModbusMasterSchedule4ConfigFile.class.getSimpleName());

	protected static String configFileName = "autoSend.txt";
	@Override
	protected int getFixedDelay() {	 
		return 0;
	}
	@Override
	protected PriorityStrategy getPriorityStrategy() {
		return PriorityStrategy.Channel;
	}
	
	@Override
	protected Logger getLogger() {

		return logger;
	}

	@Override
	protected List<String> buildReqsList() {
		return parseReqs();
	}

	protected static List<String> parseReqs() {
		List<String> configStrings = readConfig("/" + configFileName);
		List<String> reqStrings = parseReqs(configStrings);
		return reqStrings;
	}

	protected static List<String> parseReqs(List<String> configStrings) {
		List<String> reqStrings = Lists.newArrayList(configStrings);
		if (!configStrings.isEmpty()) {
			reqStrings.remove(0);
		}
		return reqStrings;
	}

	protected static List<String> readConfig(String fileName) {
		logger.info("readConfig:" + fileName);
		List<String> strList = Lists.newArrayList();
		try {
			InputStream input = InputStreamUtil.getStream(fileName);
			if (input != null)
				strList = FileUtil.readLines(input, StandardCharsets.UTF_8);
		} catch (IOException ex) {
			logger.error("readConfig:" + fileName + " ex:", ex);
		}
		return strList;
	}

	

}
