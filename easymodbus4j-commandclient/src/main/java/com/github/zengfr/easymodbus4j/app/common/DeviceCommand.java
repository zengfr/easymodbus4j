package com.github.zengfr.easymodbus4j.app.common;

public class DeviceCommand<T> {
	/** 设备标识id(必选) */
	public String deviceId;
	/** 设备ip(可选) */
	public String ip;
	/** 设备port(可选) */
	public int port;
	/** 设备型号(可选) */
	public String version;
	/** 见classFunctionCode */
	public int functionCode;
	/** 寄存器地址 */
	public int address;
	/** 寄存器地址值 */
	public T value;
	/** 寄存器地址值 */
	public T[] values;
}
