package com.github.zengfr.easymodbus4j.app.client;

import org.apache.commons.lang3.StringUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
public abstract class UdpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
		final ByteBuf buf = msg.content();
		int readableBytes = buf.readableBytes();
		byte[] content = new byte[readableBytes];
		buf.readBytes(content);
		channelRead0(ctx, new String(content, "UTF-8"));
	}

	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		if (StringUtils.isNotEmpty(msg)) {
			String[] args = msg.split(";");
			if (args.length >= 8) {
				int success = Integer.valueOf(args[0]);

				String uuid = args[1];
				String deviceId = args[2];
				String ip = args[3];
				Integer port = Integer.valueOf(args[4]);
				String version = args[5];
				Integer functionCode = Integer.valueOf(args[6]);
				Integer address = Integer.valueOf(args[7]);
				String value = args[8];
				channelRead0(uuid, deviceId, ip, port, version, functionCode, address, value, success);
			}
		}
	}

	protected abstract void channelRead0(String uuid, String deviceId, String ip, Integer port, String version,
			Integer functionCode, Integer address, String value, int success) throws Exception;
}
