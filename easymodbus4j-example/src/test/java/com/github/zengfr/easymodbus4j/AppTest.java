package com.github.zengfr.easymodbus4j;

import org.junit.Test;

import com.github.zengfr.easymodbus4j.main.Example;

public class AppTest {
	@Test
	public void testServer4MasterAndUdpServer() throws Exception {
		Example.main(new String[] { "33502,true,54321" });
	}
}
