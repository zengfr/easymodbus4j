package com.github.zengfr.easymodbus4j.app.plugin;

import java.util.Map;

import com.google.common.collect.Maps;

public class DeviceCommandPluginRegister {

	private static DeviceCommandPluginRegister instance = new DeviceCommandPluginRegister();
	private static Map<String, DeviceCommandPlugin> plugins = Maps.newHashMap();

	public static DeviceCommandPluginRegister getInstance() {
		return instance;
	}

	public void reg(Class<DeviceCommandPlugin> pluginClass) throws InstantiationException, IllegalAccessException {
		if (pluginClass != null)
			reg(pluginClass.newInstance());
	}

	public void reg(DeviceCommandPlugin plugin) {
		if (plugin != null) {
			plugins.put("", plugin);
		}
	}

	public DeviceCommandPlugin get() {
		return plugins.get("");
	}
}
