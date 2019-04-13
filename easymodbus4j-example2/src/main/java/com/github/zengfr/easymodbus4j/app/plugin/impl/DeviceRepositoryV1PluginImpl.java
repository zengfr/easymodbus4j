package com.github.zengfr.easymodbus4j.app.plugin.impl;

import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.github.zengfr.easymodbus4j.app.cache.ModbusIpPortDeviceIdCache;
import com.github.zengfr.easymodbus4j.app.cache.ModbusVersionIdCache;
import com.github.zengfr.easymodbus4j.app.cache.ModbusDeviceIdVersionIdCache;
import com.github.zengfr.easymodbus4j.app.common.DeviceArg;
import com.github.zengfr.easymodbus4j.app.plugin.DeviceRepositoryPlugin;
import com.github.zengfr.easymodbus4j.app.repository.DataRestRepository;
import com.github.zengfr.easymodbus4j.app.repository.update_modbus_valuesReq;
import com.github.zengfr.easymodbus4j.app.repository.update_modbus_valuesReqItem;
import com.github.zengfr.easymodbus4j.app.repository.update_slaveipportReq;
import com.google.common.collect.Lists;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class DeviceRepositoryV1PluginImpl implements DeviceRepositoryPlugin {
	private static final InternalLogger logger = InternalLoggerFactory
			.getInstance(DeviceRepositoryV1PluginImpl.class.getSimpleName());

	@Override
	public void updateDeviceIpAndPort(String deviceId, String ipAndPort) {
		String[] ipAndPorts = ipAndPort.split(":");
		if (ipAndPorts.length >= 2) {

			ModbusIpPortDeviceIdCache.getInstance().put(ipAndPort, deviceId, true);
			logger.info(String.format("updateIpAndPort:%s;%s;", deviceId, ipAndPort));

			update_slaveipportReq req = new update_slaveipportReq();

			req.mainboard_no = deviceId;
			req.ip = ipAndPorts[0];
			req.port = Integer.valueOf(ipAndPorts[1]);
			try {
				DataRestRepository.update_ipport(req);
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	@Override
	public void updateFuctionValue(String deviceId, short func, int address, String value) {
		logger.info(String.format("updateFuncValue:%s;%s;%s;%s", deviceId, func, address, value));

		update_modbus_valuesReq req = new update_modbus_valuesReq();
		req.time_stamp = System.currentTimeMillis();
		req.mainboard_no = deviceId;
		req.items = Lists.newArrayList();
		update_modbus_valuesReqItem item = new update_modbus_valuesReqItem();
		item.function = "" + func;
		item.address = "" + address;
		item.value = "" + value;
		req.items.add(item);
		try {
			DataRestRepository.update_values(req);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@Override
	public DeviceArg getDeviceArg(String deviceId) {
		DeviceArg arg = new DeviceArg();
		arg.port = -1;
		arg.version = getVersionId(deviceId);
		String key = ModbusIpPortDeviceIdCache.getInstance().getIpAndPort(deviceId);
		if (key != null) {
			String[] keys = key.split(":");
			if (keys.length > 1) {
				arg.ip = keys[0];
				arg.port = Integer.parseInt(keys[1]);
			}
		}
		logger.info(String.format("getDeviceArg:%s;%s", deviceId, JSON.toJSONString(arg)));
		return arg;
	}

	@Override
	public Set<String> getVersionIds() {
		return ModbusVersionIdCache.getInstance();
	}

	@Override
	public String getVersionId(String deviceId) {
		String versionId = "cx10012";
		ModbusDeviceIdVersionIdCache.getInstance().put(deviceId, versionId);
		return versionId;
	}

	@Override
	public String getDeviceIdByIpAndPort(String ipAndPort) {

		return ModbusIpPortDeviceIdCache.getInstance().getDeviceId(ipAndPort);
	}

	@Override
	public boolean isGetDeviceIdReq(short funCode, int address, int quantityOfInputRegisters) {
		return funCode == 3 && address == 69 && quantityOfInputRegisters == 2;
	}
}
