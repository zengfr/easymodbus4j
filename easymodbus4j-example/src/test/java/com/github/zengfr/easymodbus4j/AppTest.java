
package com.github.zengfr.easymodbus4j;

import org.junit.Test;

import com.github.zengfr.easymodbus4j.main.Example;

 
public class AppTest {
	@Test
	public void test4TcpMaster() throws Exception {
		Example.main(new String[] { "1,127.0.0.1,502,1,0,T,0,T,12000,54321" });
		System.in.read();
	}
	@Test
	public void test4TcpClient() throws Exception {
		Example.main(new String[] { "2,127.0.0.1,502,1,0,T,0,T,12000,54321" });
		System.in.read();
	}
}
