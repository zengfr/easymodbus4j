import org.junit.BeforeClass;
import org.junit.Test;

import com.github.zengfr.easymodbus4j.app.client.DeviceClient;
import com.github.zengfr.easymodbus4j.app.client.UdpClientHandler;
import com.github.zengfr.easymodbus4j.app.common.DeviceCommand;
import com.github.zengfr.easymodbus4j.app.common.FunctionCode;

public class ClientTest {
	static UdpClientHandler handler = new CustomUdpClientHandler();
	static DeviceClient client = DeviceClient.getInstance();

	@BeforeClass
	public static void init() throws Exception {
		client.setup(handler);
	}

	@Test
	public void test() throws Exception {
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			System.out.println(i);
			Thread.sleep(111);
			for (int j = 0; j < 111; j++) {
				DeviceCommand<Float> cmd = new DeviceCommand<>();
				cmd.setIp("11.22.33.44");
				cmd.setPort(4199);
				cmd.setAddress(1);
				cmd.setFunctionCode(FunctionCode.WRITE_MULTIPLE_REGISTERS);
				cmd.setValue(Float.valueOf("12.6"));

				client.sendCommand("192.168.77.88", 54321, cmd);
			}
		}
	}
}
