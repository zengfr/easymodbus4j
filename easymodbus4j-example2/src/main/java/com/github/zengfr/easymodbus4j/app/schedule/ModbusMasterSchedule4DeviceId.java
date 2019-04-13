package com.github.zengfr.easymodbus4j.app.schedule;

import java.util.List;

import com.github.zengfr.easymodbus4j.app.cache.ModbusVersionIdCache;
import com.github.zengfr.easymodbus4j.app.plugin.DeviceRepositoryPlugin;
import com.github.zengfr.easymodbus4j.app.plugin.DeviceRepositoryPluginRegister;
import com.github.zengfr.easymodbus4j.app.repository.DataRestRepository;
import com.github.zengfr.easymodbus4j.app.repository.mainboard_adressResp;
import com.github.zengfr.easymodbus4j.app.repository.mainboard_adressRespItem;
import com.github.zengfr.easymodbus4j.example.schedule.ModbusMasterSchedule;
import com.google.common.collect.Lists;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class ModbusMasterSchedule4DeviceId extends ModbusMasterSchedule {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(ModbusMasterSchedule4DeviceId.class.getSimpleName());

	@Override
	protected InternalLogger getlogger() {

		return logger;
	}

	@Override
	protected List<String> buildReqsList() {

		return parseReqs();
	}

	private static List<String> parseReqs() {
		List<String> reqStrings = Lists.newArrayList();
		try {
			mainboard_adressResp resp = null;
			resp = DataRestRepository.get_mainboard_addresslist();
			if (resp != null && resp.results != null) {
				for (mainboard_adressRespItem item : resp.results) {
					reqStrings.add(String.format("%s|%s|%s", item.function, item.address, item.quantity));
					ModbusVersionIdCache.getInstance().add(item.model_no);
				}

			}

		} catch (Exception e) {
			logger.error(e);
		}
		return reqStrings;
	}

	protected static DeviceRepositoryPlugin getDeviceRepositoryPlugin() {
		return DeviceRepositoryPluginRegister.getInstance().get();
	}
}
