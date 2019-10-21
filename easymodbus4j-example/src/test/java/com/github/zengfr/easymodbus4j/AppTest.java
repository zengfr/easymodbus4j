
package com.github.zengfr.easymodbus4j;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.github.zengfr.easymodbus4j.logging.ChannelLogger;
import com.github.zengfr.easymodbus4j.main.Example;

 
public class AppTest {
	final static ChannelLogger logger=ChannelLogger.getLogger(AppTest.class);
	final static Logger logger2=LoggerFactory.getLogger(AppTest.class);
	@Test
	public void testLogger() throws Exception {
		 logger.debug(null, "test", "test");
		 
		 MDC.put("channel","test2");
		 logger2.debug("1234567");
	}
	@Test
	public void test4TcpMaster() throws Exception {
		Example.main(new String[] { "1,127.0.0.1,502,1,0,T,0,T,12000,54321" });
		System.in.read();
	}
	@Test
	public void test4TcpClient() throws Exception {
		//Example.main(new String[] { "2,127.0.0.1,502,1,0,T,0,T,12000,54321" });
		//System.in.read();
	}
}
