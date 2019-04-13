package com.github.zengfr.easymodbus4j.app.plugin;

import com.github.zengfr.easymodbus4j.app.common.DeviceCommand;

import io.netty.buffer.ByteBuf;

public interface DeviceCommandPlugin extends DevicePlugin {

	public <T> boolean isEnabled(DeviceCommand<T> cmd);

	public <T> ByteBuf buildRequestFrame(DeviceCommand<T> cmd);

	public void parseResponseFrame(ByteBuf buffer);
}
