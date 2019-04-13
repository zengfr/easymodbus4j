package com.github.zengfr.easymodbus4j.app.client;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;

import com.github.zengfr.easymodbus4j.app.common.DeviceCommand;
/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
public class DeviceClient extends UdpClient {
	private static final int MAX = 10000 * 999;
	private static final int MIN = 10000 * 100;
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
	private static Random rnd = new Random();

	public <T> String sendCommand(String host, int port, DeviceCommand<T> cmd) throws InterruptedException {
		String uuid = getUUID();
		getSender().send(host, port, buildCommandMessage(uuid, cmd));
		return uuid;
	}

	public <T> String sendCommand2(String host, int port, DeviceCommand<T> cmd) throws InterruptedException {
		String uuid = getUUID();
		getSender().send(host, port, buildCommandMessage2(uuid, cmd));
		return uuid;
	}

	protected <T> String buildCommandMessage2(String uuid, DeviceCommand<T> cmd) {
		return String.format("%s;%s;%s;%s;%s;%s;%s;%s", uuid, cmd.deviceId, cmd.ip, cmd.port, cmd.version, cmd.functionCode, cmd.address,
				StringUtils.join(cmd.values, ","));
	}

	protected <T> String buildCommandMessage(String uuid, DeviceCommand<T> cmd) {
		return String.format("%s;%s;%s;%s;%s;%s;%s;%s", uuid, cmd.deviceId, cmd.ip, cmd.port,cmd.version, cmd.functionCode, cmd.address,
				cmd.value);
	}

	protected String getUUID() {
		int randNumber = rnd.nextInt(MAX - MIN + 1) + MIN;
		return String.format("%s%s", LocalDateTime.now(ZoneOffset.of("+8")).format(formatter), randNumber);
	}
}