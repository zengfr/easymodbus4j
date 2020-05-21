package com.github.zengfr.easymodbus4j.example3;

import com.github.zengfr.easymodbus4j.ModbusConfs;
import com.github.zengfr.easymodbus4j.client.ModbusClient4TcpMaster;
import com.github.zengfr.easymodbus4j.client.ModbusClientTcpFactory;
import com.github.zengfr.easymodbus4j.common.util.ConsoleUtil;
import com.github.zengfr.easymodbus4j.common.util.ScheduledUtil;
import com.github.zengfr.easymodbus4j.example.processor.ExampleModbusMasterResponseProcessor;
import com.github.zengfr.easymodbus4j.handle.impl.ModbusMasterResponseHandler;
import com.github.zengfr.easymodbus4j.handler.ModbusResponseHandler;
import com.github.zengfr.easymodbus4j.processor.ModbusMasterResponseProcessor;
import com.github.zengfr.easymodbus4j.sender.ChannelSender;
import com.github.zengfr.easymodbus4j.sender.ChannelSenderFactory;

import io.netty.channel.Channel;

public class Example3 {
	private static  ModbusClient4TcpMaster modbusClient;
	public static void main(String[] args) throws Exception {
		initClient();
		scheduleToSendData();
		//1\ ChannelSender to send data to machine
		//2\ ExampleModbusMasterResponseProcessor  to receive resp data.
		
	}
	private static void initClient() throws Exception {
		String host="123.45.67.89";
		int port=502;
		long sleep=3000;
		short transactionIdentifierOffset=0;
		ModbusConfs.MASTER_SHOW_DEBUG_LOG = true;
		ModbusMasterResponseProcessor masterProcessor=new ExampleModbusMasterResponseProcessor(transactionIdentifierOffset);
		ModbusResponseHandler responseHandler = new ModbusMasterResponseHandler(masterProcessor);;
		modbusClient = ModbusClientTcpFactory.getInstance().createClient4Master(host, port, responseHandler);
		
		Thread.sleep(sleep);  
	}
	private static void scheduleToSendData() {
		
		Runnable runnable = () ->{ 
			ConsoleUtil.clearConsole(true);
			  Channel channel = modbusClient.getChannel();
			if(channel==null||(!channel.isActive())||!channel.isOpen()||!channel.isWritable())
				return;
			ChannelSender sender = ChannelSenderFactory.getInstance().get(channel);
			//short unitIdentifier=1;
			//ChannelSender sender2 =new ChannelSender(channel, unitIdentifier);
			
			int startAddress=0;
			int quantityOfCoils=4;
			try {
				sender.readCoilsAsync(startAddress, quantityOfCoils);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		int sleep=1000;
		ScheduledUtil.getInstance().scheduleAtFixedRate(runnable, sleep * 5);
		
	}
}
