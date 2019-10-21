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

import org.apache.commons.lang3.StringUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import io.netty.channel.ChannelHandler.Sharable;
/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
@Sharable
public abstract class UdpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
		if(isDoReceived()) {
		messageReceived(ctx, packet.content().toString(CharsetUtil.UTF_8));
		}
	}

	protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
		if (StringUtils.isNotEmpty(msg)) {
			String[] args = msg.split(";");
			if (args.length >= 11) {
				int success = Integer.valueOf(args[0]);

				String uuid = args[1];
				String deviceId = args[2];
				String ip = args[3];
				Integer port = Integer.valueOf(args[4]);
				String version = args[5];
				Integer functionCode = Integer.valueOf(args[6]);
				Integer address = Integer.valueOf(args[7]);
				String valueType = args[8];
				String value = args[9];
				String[] values = args[10].split(",");
				channelRead0(uuid, deviceId, ip, port, version, functionCode, address, valueType, value, values,
						success);
				values=null;
			}
			args=null;
		}
	}
	protected abstract boolean isDoReceived();
	protected abstract void channelRead0(String uuid, String deviceId, String ip, Integer port, String version,
			Integer functionCode, Integer address, String valueType, String value, String[] values, int success)
			throws Exception;
}
