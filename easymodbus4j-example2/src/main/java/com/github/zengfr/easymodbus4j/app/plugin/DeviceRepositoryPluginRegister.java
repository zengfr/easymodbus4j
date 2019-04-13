package com.github.zengfr.easymodbus4j.app.plugin;

import java.util.Map;

import com.google.common.collect.Maps;

public class DeviceRepositoryPluginRegister {
	private static DeviceRepositoryPluginRegister instance = new DeviceRepositoryPluginRegister();
	private static Map<String, DeviceRepositoryPlugin> plugins = Maps.newHashMap();

	public static DeviceRepositoryPluginRegister getInstance() {
		return instance;
	}

	public void reg(Class<DeviceRepositoryPlugin> pluginClass) throws InstantiationException, IllegalAccessException {
		if (pluginClass != null)
			reg(pluginClass.newInstance());
	}

	public void reg(DeviceRepositoryPlugin plugin) {
		if (plugin != null)
			plugins.put("", plugin);
	}

	public DeviceRepositoryPlugin get() {
		return plugins.get("");
	}
}
