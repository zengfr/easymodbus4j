package com.github.zengfr.easymodbus4j.app.sender;

import java.util.Map;
import com.google.common.collect.Maps;

import io.netty.channel.Channel;

public class UdpSenderFactory {
	private static class UdpSenderFactoryHolder {
		private static final UdpSenderFactory INSTANCE = new UdpSenderFactory();
	}

	public static UdpSenderFactory getInstance() {
		return UdpSenderFactoryHolder.INSTANCE;
	}

	private Map<String, UdpSender> channelSendersMap = Maps.newHashMap();

	public UdpSender get(Channel channel) {
		String key = channel.toString();
		if (!channelSendersMap.containsKey(key)) {
			channelSendersMap.put(key, new UdpSender(channel));
		}
		return channelSendersMap.get(key);
	}
}
