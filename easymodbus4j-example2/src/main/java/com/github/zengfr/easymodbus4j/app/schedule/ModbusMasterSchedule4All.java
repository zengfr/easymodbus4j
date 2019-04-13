package com.github.zengfr.easymodbus4j.app.schedule;

import java.util.List;
import java.util.Set;

import com.github.zengfr.easymodbus4j.app.plugin.DeviceRepositoryPlugin;
import com.github.zengfr.easymodbus4j.app.plugin.DeviceRepositoryPluginRegister;
import com.github.zengfr.easymodbus4j.app.repository.DataRestRepository;
import com.github.zengfr.easymodbus4j.app.repository.autosend_listResp;
import com.github.zengfr.easymodbus4j.app.repository.autosend_listRespItem;
import com.github.zengfr.easymodbus4j.example.schedule.ModbusMasterSchedule;
import com.google.common.collect.Lists;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class ModbusMasterSchedule4All extends ModbusMasterSchedule {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(ModbusMasterSchedule4All.class.getSimpleName());

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
			autosend_listResp resp = null;
			Set<String> versionIds = getDeviceRepositoryPlugin().getVersionIds();
			for (String versionId : versionIds) {
				resp = DataRestRepository.get_autosendlist(versionId);
				if (resp != null && resp.results != null) {
					for (autosend_listRespItem item : resp.results) {
						reqStrings.add(String.format("%s|%s|%s", item.function, item.address, item.quantity));
					}
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
