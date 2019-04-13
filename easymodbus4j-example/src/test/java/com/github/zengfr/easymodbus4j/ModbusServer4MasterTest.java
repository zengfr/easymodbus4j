package com.github.zengfr.easymodbus4j;

import org.junit.Before;
import org.junit.Test;

public class ModbusServer4MasterTest extends ModbusSetupTestCase {
	@Before
	public void setUp() throws Exception {
		super.init();
		int port = ModbusConstants.DEFAULT_MODBUS_PORT3;
		super.setUpServer4Master(port);
		super.setUpClient4Slave(port);
	}

	@Test
	public void testServer4Master() throws Exception {
		super.testServer4Master(22);
	}
}
