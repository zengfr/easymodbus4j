
package com.github.zengfr.easymodbus4j;
import org.junit.Test;

import com.github.zengfr.easymodbus4j.main.Example2;

 
public class AppTest {
	@Test
	public void testServer4MasterAndUdpServer() throws Exception {
		Example2.main(new String[] { "502,T,T,10000,54321,1,1" });
	}
}
