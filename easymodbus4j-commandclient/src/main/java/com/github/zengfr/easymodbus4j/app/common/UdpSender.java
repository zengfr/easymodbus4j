package com.github.zengfr.easymodbus4j.app.common;

import java.net.InetSocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class UdpSender {
	protected Channel channel;

	public UdpSender(Channel channel) {
		this.channel = channel;
	}

	public void send(DatagramPacket packet) throws InterruptedException {
		channel.writeAndFlush(packet).sync();
	}

	public void send(String host, int port, String msg) throws InterruptedException {
		send(new InetSocketAddress(host, port), msg);
	}

	public void send(InetSocketAddress address, String msg) throws InterruptedException {
		send(address, Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
	}

	public void send(String host, int port, ByteBuf byteBuf) throws InterruptedException {
		send(new InetSocketAddress(host, port), byteBuf);
	}

	public void send(InetSocketAddress address, ByteBuf byteBuf) throws InterruptedException {
		send(new DatagramPacket(byteBuf, address));
	}
}
