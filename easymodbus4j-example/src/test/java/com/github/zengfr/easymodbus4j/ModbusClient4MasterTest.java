package com.github.zengfr.easymodbus4j;

import org.junit.Before;
import org.junit.Test;

public class ModbusClient4MasterTest extends ModbusSetupTestCase {
	@Before
	public void setup() throws Exception {
		super.init((short)0);
		int port = ModbusConstants.DEFAULT_MODBUS_PORT5;
		super.setupServer4Slave(port);
		super.setupClient4Master(port);
		
	}

	@Test
	public void testClient4Master() throws Exception {
		super.testClient4Master(22);
	}

}
