package com.github.zengfr.easymodbus4j;

import org.junit.Before;
import org.junit.Test;

public class ModbusClient4MasterTest extends ModbusSetupTestCase {
	@Before
	public void setUp() throws Exception {
		super.init();
		int port = ModbusConstants.DEFAULT_MODBUS_PORT5;
		super.setUpServer4Slave(port);
		super.setUpClient4Master(port);
		
	}

	@Test
	public void testClient4Master() throws Exception {
		super.testClient4Master(22);
	}

}
