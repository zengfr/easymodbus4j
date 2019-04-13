package com.github.zengfr.easymodbus4j.example.schedule;

import java.util.Collection;
import java.util.List;

import com.github.zengfr.easymodbus4j.util.ModbusRequestSendUtil;
import com.github.zengfr.easymodbus4j.util.ScheduledUtil;

import io.netty.channel.Channel;
import io.netty.util.internal.logging.InternalLogger;

public abstract class ModbusMasterSchedule {
	protected abstract List<String> buildReqsList();

	protected abstract InternalLogger getlogger();

	public void schedule(Collection<Channel> channels, int sleep) {
		sendRequests4Auto(channels, sleep, buildReqsList());
	}

	public void run(Collection<Channel> channels) {
		List<String> reqStrs = buildReqsList();
		getlogger().info(String.format("channels:%s,reqStrs:%s", channels.size(), reqStrs.size()));
		ModbusRequestSendUtil.sendRequests(channels, reqStrs);
	}

	private void sendRequests4Auto(Collection<Channel> channels, int sleep, List<String> reqStrs) {
		Runnable r = () -> {
			getlogger()
					.info(String.format("channels:%s,reqStrs:%s,sleep:%s ms", channels.size(), reqStrs.size(), sleep));
			ModbusRequestSendUtil.sendRequests(channels, reqStrs);
		};
		ScheduledUtil.scheduleWithFixedDelay(r, sleep);
	}
}