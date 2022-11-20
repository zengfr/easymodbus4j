
package com.github.zengfr.easymodbus4j.app;
import org.junit.Test;

import com.github.zengfr.easymodbus4j.main.Example2;

 
public class AppTest {
	
	 
	
	@Test
	public void testServer4MasterAndUdpServer() throws Exception {
		Example2.main(new String[] { "0,127.0.0.1,502,1,1,T,T,25000,54321" });
		 
			 
			 
			 
	}
   
}
