package com.github.zengfr.easymodbus4j.app.cache;

import java.util.HashMap;

public class ModbusDeviceIdVersionIdCache extends HashMap<String, String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6593701804114127821L;

	private static class ModbusVersionIdCacheHolder {
		private static final ModbusDeviceIdVersionIdCache INSTANCE = new ModbusDeviceIdVersionIdCache();
	}

	public static ModbusDeviceIdVersionIdCache getInstance() {
		return ModbusVersionIdCacheHolder.INSTANCE;
	}
}
