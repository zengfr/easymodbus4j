package com.github.zengfr.easymodbus4j.app.cache;

import java.util.Set;

import com.google.common.collect.BiMap;

public abstract class AbstrctModbusKVCache {
	protected abstract BiMap<String, String> getCacheMap();

	public boolean containsValue(String v) {
		return getCacheMap().containsValue(v);
	}

	public boolean containsKey(String k) {
		return getCacheMap().containsKey(k);
	}

	public void put(String k, String v, boolean forcePut) {
		if (forcePut)
			getCacheMap().forcePut(k, v);
		else
			getCacheMap().put(k, v);
	}

	protected String getKey(String deviceId) {
		return getCacheMap().inverse().get(deviceId);
	}

	protected String getValue(String k) {
		return getCacheMap().get(k);
	}

	protected Set<String> getValues() {
		return getCacheMap().values();
	}

}
