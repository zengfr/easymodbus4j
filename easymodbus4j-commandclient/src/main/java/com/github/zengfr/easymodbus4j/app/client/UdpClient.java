package com.github.zengfr.easymodbus4j.app.client;

import com.github.zengfr.easymodbus4j.app.common.UdpSender;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
public class UdpClient {
	protected Channel channel;
	protected UdpSender sender;
	public void setup(UdpClientHandler handler) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioDatagramChannel.class).handler(handler);
		channel = b.bind(0).sync().channel();
		sender=new UdpSender(channel);
		//channel.closeFuture().await();
	}

	public UdpSender getSender() {
		return this.sender;
	}

	public Channel getChannel() {
		return channel;
	}

	
}
