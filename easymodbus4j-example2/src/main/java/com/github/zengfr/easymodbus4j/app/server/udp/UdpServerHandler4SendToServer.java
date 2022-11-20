/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.zengfr.easymodbus4j.app.server.udp;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.zengfr.easymodbus4j.app.common.DeviceArg;
import com.github.zengfr.easymodbus4j.app.common.DeviceCommand;
import com.github.zengfr.easymodbus4j.app.plugin.DeviceCommandPlugin;
import com.github.zengfr.easymodbus4j.app.plugin.DeviceCommandPluginRegister;
import com.github.zengfr.easymodbus4j.app.plugin.DeviceRepositoryPluginRegister;
import com.github.zengfr.easymodbus4j.app.sender.UdpSender;
import com.github.zengfr.easymodbus4j.app.sender.UdpSenderFactory;
import com.github.zengfr.easymodbus4j.codec.tcp.ModbusTcpDecoder;
import com.github.zengfr.easymodbus4j.protocol.tcp.ModbusFrame;
import com.github.zengfr.easymodbus4j.sender.ChannelSender;
import com.github.zengfr.easymodbus4j.sender.ChannelSenderFactory;
import com.google.common.collect.Lists;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
public class UdpServerHandler4SendToServer extends UdpServerHandler {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(UdpServerHandler4SendToServer.class.getSimpleName());
	protected Collection<Channel> serverClientchannels = Lists.newArrayList();

	public UdpServerHandler4SendToServer(Collection<Channel> serverClientchannels) {
		this.serverClientchannels = serverClientchannels;
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, InetSocketAddress sender, String msg) throws Exception {
		super.messageReceived(ctx, sender, msg);
		int success = processMessage(ctx, msg);
		UdpSender udpSender = UdpSenderFactory.getInstance().get(ctx.channel());
		udpSender.send(sender, String.format("%s;%s", success, msg));
	}

	protected int processMessage(ChannelHandlerContext ctx, String msg) {
		int success = -1;
		if (StringUtils.isNotEmpty(msg)) {
			DeviceCommand<String> cmd = parseCommand(msg);
			if (isEnabled(cmd)) {
				String ip = cmd.getIp();
				int port = cmd.getPort();
				String versionId = cmd.getVersion();
				int funcCode = cmd.getFunctionCode();
				DeviceArg deviceArg = getDeviceArg(cmd.getDeviceId());
				if (deviceArg != null && deviceArg.port >= 0) {
					ip = deviceArg.ip;
					port = deviceArg.port;
					versionId = deviceArg.version != null ? deviceArg.version : versionId;
				}
				if (funcCode < 0) {
					Collection<Channel> channels = getChannels(ip, port);
					success = 0;
					for (Channel channel : channels) {
						channel.close();
						success++;
					}
				} 
				if (funcCode > 0) {
					ModbusFrame reqFrame = buildRequestFrame(versionId, cmd);
					logger.info(String.format("v1219;%s;%s;%s;%s;%s;", ip, port, versionId, cmd.getValueType(), reqFrame));
					if (reqFrame != null) {
						Collection<Channel> channels = getChannels(ip, port);
						success = 0;
						for (Channel channel : channels) {
							ChannelSender sender = ChannelSenderFactory.getInstance().get(channel);
							sender.sendModbusFrame(reqFrame);
							success++;
						}

					} else {
						success--;
					}
				}

			} else {
				success--;
			}

		} else {
			success--;
		}
		return success;
	}

	protected DeviceCommand<String> parseCommand(String msg) {
		DeviceCommand<String> cmd = new DeviceCommand<String>();
		cmd.setFunctionCode(-1);
		if (StringUtils.isNotEmpty(msg)) {
			String[] args = msg.split(";");
			if (args.length >= 4) {
				cmd.setDeviceId(args[1]);
				cmd.setIp(args[2]);
				cmd.setPort(Integer.valueOf(args[3]));
			}
			if (args.length >= 10) {// uuid-deviceId-ip-prot-vserion-func-address-value
				cmd.setDeviceId(args[1]);
				cmd.setIp(args[2]);
				cmd.setPort(Integer.valueOf(args[3]));
				cmd.setVersion(args[4]);
				cmd.setFunctionCode(Integer.valueOf(args[5]));
				cmd.setAddress(Integer.valueOf(args[6]));
				
				cmd.setValue(args[8]);
				cmd.setValues(args[9].split(","));
				cmd.setValueType(args[7]);
			}
		}
		return cmd;
	}

	protected Collection<Channel> getChannels(String host, int port) {
		List<Channel> channels = Lists.newArrayList();
		String key;
		if (port > 0) {
			key = String.format("/%s:%s", host, port);
			for (Channel channel : this.serverClientchannels) {
				if (channel.remoteAddress().toString().equalsIgnoreCase(key)) {
					channels.add(channel);
					break;
				}
			}
		} else {
			key = String.format("/%s:", host);
			for (Channel channel : this.serverClientchannels) {
				if (channel.remoteAddress().toString().contains(key)) {
					channels.add(channel);
				}
			}
		}
		return channels;
	}

	protected ModbusFrame buildRequestFrame(String version, DeviceCommand<String> cmd) {
		DeviceCommandPlugin deviceCommandPlugin = getDeviceCommandPlugin();
		if (deviceCommandPlugin != null) {
			ByteBuf buffer = deviceCommandPlugin.buildRequestFrame(cmd);
			boolean isSlave = true;
			ModbusFrame frame = ModbusTcpDecoder.decodeFrame(buffer, isSlave);
			return frame;
		}
		return null;
	}

	protected <T> boolean isEnabled(DeviceCommand<T> cmd) {
		if (cmd != null) {
			DeviceCommandPlugin deviceCommandPlugin = getDeviceCommandPlugin();
			if (deviceCommandPlugin != null) {
				return deviceCommandPlugin.isEnabled(cmd);
			}
		}
		return false;
	}

	protected DeviceCommandPlugin getDeviceCommandPlugin() {
		return DeviceCommandPluginRegister.getInstance().get();
	}

	protected DeviceArg getDeviceArg(String deviceId) {
		return DeviceRepositoryPluginRegister.getInstance().get().getDeviceArg(deviceId);
	}
}
