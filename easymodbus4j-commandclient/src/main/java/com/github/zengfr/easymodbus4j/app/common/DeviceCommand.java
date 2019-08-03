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
package com.github.zengfr.easymodbus4j.app.common;

import org.apache.commons.lang3.StringUtils;

public class DeviceCommand<T> {
	/** 设备标识id(必选) */
	private String deviceId;
	/** 设备ip(可选) */
	private String ip;
	/** 设备port(可选) */
	private int port;

	/** 设备型号(可选) */
	private String version;
	/** 见classFunctionCode */
	private int functionCode;
	/** 寄存器地址 */
	private int address;
	/** 寄存器地址值 */
	private T value;
	/** 寄存器地址值 */
	private T[] values;
	/** 值类型 */
	private String valueType;

	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getFunctionCode() {
		return this.functionCode;
	}

	public void setFunctionCode(int functionCode) {
		this.functionCode = functionCode;
	}

	public int getAddress() {
		return this.address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public T getValue() {
		return this.value;
	}

	public void setValue(T value) {
		this.value = value;
		syncValueType();
	}

	public T[] getValues() {
		return this.values;
	}

	public void setValues(T[] values) {
		this.values = values;
		syncValueType();
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public String getValueType() {
		return valueType;
	}

	public void syncValueType() {
		setValueType(getValueType(this.value, this.values));
	}

	@Override
	public String toString() {
		return String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s", deviceId, ip, port, version, functionCode, address,
				getValueType(), value, StringUtils.join(values, ','));
	}

	private static <T> String getValueType(T v, T[] vv) {
		if (v != null && !v.toString().isEmpty()) {
			return v.getClass().getSimpleName();
		} else if (vv != null && vv.length > 0) {
			return vv[0].getClass().getSimpleName();
		}
		return "";
	}

}
