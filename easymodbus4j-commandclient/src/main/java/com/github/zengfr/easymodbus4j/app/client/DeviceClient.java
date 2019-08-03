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
package com.github.zengfr.easymodbus4j.app.client;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import com.github.zengfr.easymodbus4j.app.common.DeviceCommand;
/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
public class DeviceClient extends UdpClient {
	private static final int MAX = 10000 * 999;
	private static final int MIN = 10000 * 100;
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
	private static Random rnd = new Random();

	public <T> String sendCommand(String host, int port, DeviceCommand<T> cmd) throws InterruptedException {
		String uuid = getUUID();
		getSender().send(host, port, buildCommandMessage(uuid, cmd));
		return uuid;
	}
	protected <T> String buildCommandMessage(String uuid, DeviceCommand<T> cmd) {
		return String.format("%s;%s", uuid, cmd.toString());
	}
	protected String getUUID() {
		int randNumber = rnd.nextInt(MAX - MIN + 1) + MIN;
		return String.format("%s%s", LocalDateTime.now(ZoneOffset.of("+8")).format(formatter), randNumber);
	}
}