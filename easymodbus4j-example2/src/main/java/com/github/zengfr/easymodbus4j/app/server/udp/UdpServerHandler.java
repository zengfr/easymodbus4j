package com.github.zengfr.easymodbus4j.app.server.udp;

import java.net.InetSocketAddress;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	private static final InternalLogger logger = InternalLoggerFactory
			.getInstance(UdpServerHandler.class.getSimpleName());

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
		final ByteBuf buf = msg.content();
		int readableBytes = buf.readableBytes();
		byte[] content = new byte[readableBytes];
		buf.readBytes(content);
		channelRead0(ctx, msg.sender(), new String(content, "UTF-8"));
	}

	protected void channelRead0(ChannelHandlerContext ctx, InetSocketAddress sender, String msg) throws Exception {
		logger.info(String.format("%s->%s", sender, msg));
	}
}
