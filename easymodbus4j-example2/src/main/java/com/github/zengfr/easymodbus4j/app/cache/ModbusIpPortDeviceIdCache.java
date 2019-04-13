package com.github.zengfr.easymodbus4j.app.cache;

import java.util.Set;

import com.google.common.collect.BiMap;

public class ModbusIpPortDeviceIdCache extends AbstrctModbusKVCache {
	private static class ModbusDeviceIdCacheHolder {
		private static final ModbusIpPortDeviceIdCache INSTANCE = new ModbusIpPortDeviceIdCache();
	}

	public static ModbusIpPortDeviceIdCache getInstance() {
		return ModbusDeviceIdCacheHolder.INSTANCE;
	}

	@Override
	protected BiMap<String, String> getCacheMap() {
		return ModbusKVCacheFactory.getInstance().getBiMap(this.getClass().getSimpleName());
	}

	public String getDeviceId(String ipAndPort) {
		return getValue(ipAndPort);
	}

	public Set<String> getDeviceIds() {
		return getValues();
	}

	public String getIpAndPort(String deviceId) {
		return getKey(deviceId);
	}
}
