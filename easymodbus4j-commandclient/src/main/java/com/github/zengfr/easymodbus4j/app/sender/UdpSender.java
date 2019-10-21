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
package com.github.zengfr.easymodbus4j.app.sender;

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
