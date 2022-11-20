package com.github.zengfr.easymodbus4j.app.schedule;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.zengfr.easymodbus4j.app.cache.ModbusVersionIdCache;
import com.github.zengfr.easymodbus4j.app.plugin.DeviceRepositoryPlugin;
import com.github.zengfr.easymodbus4j.app.plugin.DeviceRepositoryPluginRegister;
import com.github.zengfr.easymodbus4j.app.repository.DataRestRepository;
import com.github.zengfr.easymodbus4j.app.repository.mainboard_adressResp;
import com.github.zengfr.easymodbus4j.app.repository.mainboard_adressRespItem;
import com.github.zengfr.easymodbus4j.schedule.ModbusMasterSchedule;
import com.github.zengfr.easymodbus4j.sender.util.ModbusRequestSendUtil.PriorityStrategy;
import com.google.common.collect.Lists;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class ModbusMasterSchedule4DeviceId extends ModbusMasterSchedule {
	private static Logger logger=LoggerFactory.getLogger(ModbusMasterSchedule4DeviceId.class.getSimpleName());
	@Override
	protected int getFixedDelay() {
		 
		return 300;
	}
	@Override
	protected PriorityStrategy getPriorityStrategy() {
		return PriorityStrategy.Req;
	}
	@Override
	protected Logger getLogger() {

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
			if (resp != null && resp.results != null && !resp.results.isEmpty()) {
				for (mainboard_adressRespItem item : resp.results) {
					reqStrings.add(String.format("%s|%s|%s", item.function, item.address, item.quantity));
					ModbusVersionIdCache.getInstance().add(item.model_no);
				}
			}else {
				logger.error("get_mainboard_addresslist isEmpty");
			}

		} catch (Exception e) {
			logger.error("parseReqs",e);
		}
		return reqStrings;
	}

	protected static DeviceRepositoryPlugin getDeviceRepositoryPlugin() {
		return DeviceRepositoryPluginRegister.getInstance().get();
	}
	
}
