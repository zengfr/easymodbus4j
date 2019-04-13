package com.github.zengfr.easymodbus4j;

import org.junit.Before;
import org.junit.Test;

public class ModbusServer4MasterTest extends ModbusSetupTestCase {
	@Before
	public void setup() throws Exception {
		super.init((short)0);
		int port = ModbusConstants.DEFAULT_MODBUS_PORT3;
		super.setupServer4Master(port);
		super.setupClient4Slave(port);
	}

	@Test
	public void testServer4Master() throws Exception {
		super.testServer4Master(2222);
	}
}
