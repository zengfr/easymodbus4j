package com.github.zengfr.easymodbus4j.example;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

import com.github.zengfr.easymodbus4j.channel.ChannelSender;
import com.github.zengfr.easymodbus4j.util.FileUtil;
import com.github.zengfr.easymodbus4j.util.InputStreamUtil;
import com.github.zengfr.easymodbus4j.util.ModbusUtil;
import com.github.zengfr.easymodbus4j.util.ParseStringUtil;
import com.google.common.collect.Lists;

import io.netty.channel.Channel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class ModbusMasterAutoSender {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(ModbusMasterAutoSender.class);
	protected static Class<ChannelSender> channelSenderClazz = ChannelSender.class;
	protected static String configFileName = "autoSend.txt";
	

	public static void sendRequests4Auto(Collection<Channel> channels, int sleep) throws InterruptedException {
		do {
			List<String> configStrings = readConfig( "/" + configFileName);
			List<String> reqStrings = parseReqs(configStrings);
			sendRequests(channels, reqStrings);
			logger.info("sleep:" + sleep + " ms");
			reqStrings = null;
			Thread.sleep(sleep);
		} while (true);
	}

	public static void sendRequests(Collection<Channel> channels, List<String> reqs) {
		try {
			for (Channel channel : channels) {
				ChannelSender sender = new ChannelSender(channel);
				for (String str : reqs) {
					String[] args = str.split("[;|]");
					if (args.length >= 3) {
						String funcString = args[0];
						String addressString = args[1];
						String value = args[2];
						sendFunc(sender, funcString, addressString, value);
					}
				}
				sender = null;
			}
		} catch (Exception ex) {
			logger.error("sendRequests:" + ex);
		}
	}

	protected static List<String> parseReqs(List<String> configStrings) {

		List<String> reqStrings = Lists.newArrayList(configStrings);
		if (!configStrings.isEmpty()) {
			reqStrings.remove(0);
		}
		logger.info("parseReqs size:" + reqStrings.size());
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
			logger.info("readConfig:" + fileName + " ex:" + ex);
		}
		return strList;
	}

	public static void sendFunc(ChannelSender sender, String funcString, String addressStr, String value)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException {
		if (!funcString.startsWith("#")) {
			Integer address = Integer.valueOf(addressStr);

			if ("TF".contains(value)) {
				sendFunc(sender, funcString, address, value.equalsIgnoreCase("T"));
			} else if (value.contains(",")) {
				String[] values = value.split("[,]");
				if (value.contains("T") || value.contains("F")) {
					sendFunc(sender, funcString, address, ParseStringUtil.parseBooleanArray(values));
				} else {
					sendFunc(sender, funcString, address, ModbusUtil.toIntArray(values));
				}
			} else {
				sendFunc(sender, funcString, address, Integer.valueOf(value));
			}
		}
	}

	public static void sendFunc(ChannelSender sender, String func, int address, int value)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException {
		Method method = channelSenderClazz.getMethod(func, int.class, int.class);
		method.invoke(sender, address, value);
	}

	public static void sendFunc(ChannelSender sender, String func, int address, boolean value)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException {

		Method method = channelSenderClazz.getMethod(func, int.class, boolean.class);
		method.invoke(sender, address, value);
	}

	public static void sendFunc(ChannelSender sender, String func, int address, int[] values)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException {

		Method method = channelSenderClazz.getMethod(func, int.class, int.class, int[].class);
		method.invoke(sender, address, values.length, (Object) values);
	}

	public static void sendFunc(ChannelSender sender, String func, int address, boolean[] values)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException {

		Method method = channelSenderClazz.getMethod(func, int.class, int.class, boolean[].class);
		method.invoke(sender, address, values.length, (Object) values);
	}

}
