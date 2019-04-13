package com.github.zengfr.easymodbus4j.example;

import com.alibaba.fastjson.JSON;
import com.github.zengfr.easymodbus4j.ModbusConstants;
import com.github.zengfr.easymodbus4j.util.ParseStringUtil;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class ModbusConfig {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(ModbusConfig.class.getSimpleName());
	public int type;
	public String host;
	public int port;
	public boolean showDebugLog;
	public boolean autoSend;
	public int sleep;

	public short unit_IDENTIFIER;
	public short transactionIdentifierOffset;

	public int udpPort;

	public static ModbusConfig parse(String[] argsArray) {
		ModbusConfig cfg = new ModbusConfig();

		cfg.type = ParseStringUtil.parseInt(argsArray, 0, 0);
		cfg.host = ParseStringUtil.parseString(argsArray, 1, ModbusConstants.DEFAULT_HOTST_IP);
		cfg.port = ParseStringUtil.parseInt(argsArray, 2, ModbusConstants.DEFAULT_MODBUS_PORT);
		cfg.unit_IDENTIFIER = ParseStringUtil.parseShort(argsArray, 3, ModbusConstants.DEFAULT_UNIT_IDENTIFIER);
		cfg.transactionIdentifierOffset = ParseStringUtil.parseShort(argsArray, 4, (short) 0);
		cfg.showDebugLog = ParseStringUtil.parseBoolean(argsArray, 5, true);

		cfg.autoSend = ParseStringUtil.parseBoolean(argsArray, 6, true);
		cfg.sleep = ParseStringUtil.parseInt(argsArray, 7, 1000 * 15);

		cfg.udpPort = ParseStringUtil.parseInt(argsArray, 8, ModbusConstants.DEFAULT_MODBUS_PORT);

		logger.info(JSON.toJSONString(cfg));
		return cfg;
	}
}
