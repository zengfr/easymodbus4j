package com.github.zengfr.easymodbus4j.app.cache;

import java.util.Map;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class ModbusKVCacheFactory {

	private static class ModbusKVCacheFactoryHolder {
		private static final ModbusKVCacheFactory INSTANCE = new ModbusKVCacheFactory();
	}

	protected static final InternalLogger logger = InternalLoggerFactory.getInstance(ModbusKVCacheFactory.class);

	public static ModbusKVCacheFactory getInstance() {
		return ModbusKVCacheFactoryHolder.INSTANCE;
	}

	private Map<String, BiMap<String, String>> cacheBiMap = Maps.newHashMap();

	public BiMap<String, String> getBiMap(String type) {
		if (!cacheBiMap.containsKey(type)) {
			cacheBiMap.put(type, HashBiMap.create());
		}
		return cacheBiMap.get(type);
	}
}
