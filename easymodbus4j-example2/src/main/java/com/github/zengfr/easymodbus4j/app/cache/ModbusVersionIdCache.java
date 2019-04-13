package com.github.zengfr.easymodbus4j.app.cache;

import java.util.HashSet;

public class ModbusVersionIdCache extends HashSet<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8199263028001745303L;

	private static class ModbusVersionIdCacheHolder {
		private static final ModbusVersionIdCache INSTANCE = new ModbusVersionIdCache();
	}

	public static ModbusVersionIdCache getInstance() {
		return ModbusVersionIdCacheHolder.INSTANCE;
	}
}
