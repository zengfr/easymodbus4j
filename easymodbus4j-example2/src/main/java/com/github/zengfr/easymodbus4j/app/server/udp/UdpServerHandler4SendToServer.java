package com.github.zengfr.easymodbus4j.app.server.udp;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.zengfr.easymodbus4j.app.common.DeviceArg;
import com.github.zengfr.easymodbus4j.app.common.DeviceCommand;
import com.github.zengfr.easymodbus4j.app.common.UdpSender;
import com.github.zengfr.easymodbus4j.app.plugin.DeviceCommandPlugin;
import com.github.zengfr.easymodbus4j.app.plugin.DeviceCommandPluginRegister;
import com.github.zengfr.easymodbus4j.app.plugin.DeviceRepositoryPluginRegister;
import com.github.zengfr.easymodbus4j.channel.ChannelSender;
import com.github.zengfr.easymodbus4j.channel.ChannelSenderFactory;
import com.github.zengfr.easymodbus4j.codec.ModbusDecoder;
import com.github.zengfr.easymodbus4j.protocol.ModbusFrame;
import com.google.common.collect.Lists;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class UdpServerHandler4SendToServer extends UdpServerHandler {
	private static final InternalLogger logger = InternalLoggerFactory
			.getInstance(UdpServerHandler4SendToServer.class.getSimpleName());
	protected Collection<Channel> serverClientchannels = Lists.newArrayList();

	public UdpServerHandler4SendToServer(Collection<Channel> serverClientchannels) {
		this.serverClientchannels = serverClientchannels;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, InetSocketAddress sender, String msg) throws Exception {
		super.channelRead0(ctx, sender, msg);
		int success = processMessage(ctx, msg);
		UdpSender udpSender = new UdpSender(ctx.channel());
		udpSender.send(sender, String.format("%s;%s", success, msg));
	}

	protected int processMessage(ChannelHandlerContext ctx, String msg) {
		int success = -1;
		if (StringUtils.isNotEmpty(msg)) {
			DeviceCommand<String> cmd = parseCommand(msg);
			if (isEnabled(cmd)) {
				String versionId = cmd.version;
				String ip = cmd.ip;
				int port = cmd.port;
				DeviceArg deviceArg = getDeviceArg(cmd.deviceId);
				if (deviceArg != null && deviceArg.port >= 0) {
					versionId = deviceArg.version;
					ip = deviceArg.ip;
					port = deviceArg.port;
				}

				ModbusFrame reqFrame = buildRequestFrame(versionId, cmd);
				logger.info(String.format("v0604;%s;%s;%s;%s;", ip, port, versionId, reqFrame));
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
		if (StringUtils.isNotEmpty(msg)) {
			String[] args = msg.split(";");
			if (args.length >= 8) {// uuid-deviceId-ip-prot-vserion-func-address-value
				cmd.deviceId = args[1];
				cmd.ip = args[2];
				cmd.port = Integer.valueOf(args[3]);
				cmd.version = args[4];
				cmd.functionCode = Integer.valueOf(args[5]);
				cmd.address = Integer.valueOf(args[6]);
				cmd.value = "" + args[7];
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
			ModbusFrame frame = ModbusDecoder.decode(buffer, isSlave);
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
