package com.github.zengfr.easymodbus4j.example.schedule;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.github.zengfr.easymodbus4j.util.FileUtil;
import com.github.zengfr.easymodbus4j.util.InputStreamUtil;
import com.google.common.collect.Lists;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class ModbusMasterSchedule4ConfigFile extends ModbusMasterSchedule {
	private static final InternalLogger logger = InternalLoggerFactory
			.getInstance(ModbusMasterSchedule4ConfigFile.class.getSimpleName());

	protected static String configFileName = "autoSend.txt";

	@Override
	protected InternalLogger getlogger() {

		return logger;
	}

	@Override
	protected List<String> buildReqsList() {
		return parseReqs();
	}

	protected static List<String> parseReqs() {
		List<String> configStrings = readConfig("/" + configFileName);
		List<String> reqStrings = parseReqs(configStrings);
		return reqStrings;
	}

	protected static List<String> parseReqs(List<String> configStrings) {
		List<String> reqStrings = Lists.newArrayList(configStrings);
		if (!configStrings.isEmpty()) {
			reqStrings.remove(0);
		}
		return reqStrings;
	}

	protected static List<String> readConfig(String fileName) {
		logger.info("readConfig:" + fileName);
		List<String> strList = Lists.newArrayList();
		try {
			InputStream input = InputStreamUtil.getStream(fileName);
			if (input != null)
				strList = FileUtil.readLines(input, StandardCharsets.UTF_8);
		} catch (IOException ex) {
			logger.error("readConfig:" + fileName + " ex:", ex);
		}
		return strList;
	}

}
