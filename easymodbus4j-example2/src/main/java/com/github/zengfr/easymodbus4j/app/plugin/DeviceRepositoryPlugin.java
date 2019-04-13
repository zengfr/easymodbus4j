package com.github.zengfr.easymodbus4j.app.plugin;

import java.util.Set;

import com.github.zengfr.easymodbus4j.app.common.DeviceArg;
/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
public interface DeviceRepositoryPlugin {
	public Set<String> getVersionIds();

	public String getVersionId(String deviceId);

	public DeviceArg getDeviceArg(String deviceId);

	public String getDeviceIdByIpAndPort(String ipAndPort);

	public void updateDeviceIpAndPort(String deviceId, String ipAndPort);

	public void updateFuctionValue(String deviceId, short func, int address, String value);

	boolean isGetDeviceIdReq(short funCode, int address, int quantityOfInputRegisters);
}