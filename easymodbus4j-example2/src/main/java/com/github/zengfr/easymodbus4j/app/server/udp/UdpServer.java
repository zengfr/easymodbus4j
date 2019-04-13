package com.github.zengfr.easymodbus4j.app.server.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class UdpServer {
	private static final InternalLogger logger = InternalLoggerFactory.getInstance(UdpServer.class.getSimpleName());

	public void setup(int port, SimpleChannelInboundHandler<DatagramPacket> handler) throws InterruptedException {
		Bootstrap b = new Bootstrap();
		EventLoopGroup group = new NioEventLoopGroup();
		b.group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, true).handler(handler);
		b.bind(port).sync();// .channel().closeFuture().await();
		logger.info(String.format("UdpServer bind:%s", port));
	}
}
