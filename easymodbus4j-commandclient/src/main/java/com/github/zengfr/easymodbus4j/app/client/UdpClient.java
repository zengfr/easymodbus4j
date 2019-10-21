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
package com.github.zengfr.easymodbus4j.app.client;

import com.github.zengfr.easymodbus4j.app.sender.UdpSender;
import com.github.zengfr.easymodbus4j.app.sender.UdpSenderFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
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
	protected boolean isInit = false;

	public void setup(UdpClientHandler handler) throws InterruptedException {
		setup(handler, false);
	}

	public void setup(UdpClientHandler handler, boolean wait) throws InterruptedException {
		if (!isInit) {
			EventLoopGroup group = new NioEventLoopGroup();
			Bootstrap b = new Bootstrap();
			b.option(ChannelOption.SO_BROADCAST, true);
			b.group(group).channel(NioDatagramChannel.class).handler(handler);
			channel = b.bind(0).sync().channel();
			sender = UdpSenderFactory.getInstance().get(channel);
			isInit = true;
			if (wait) {
				channel.closeFuture().await();
			}
		}
	}

	public UdpSender getSender() {
		return this.sender;
	}

	public Channel getChannel() {
		return channel;
	}

}
