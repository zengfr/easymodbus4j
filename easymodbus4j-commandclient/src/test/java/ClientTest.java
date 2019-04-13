import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.zengfr.easymodbus4j.app.client.DeviceClient;
import com.github.zengfr.easymodbus4j.app.client.UdpClientHandler;
import com.github.zengfr.easymodbus4j.app.common.DeviceCommand;

public class ClientTest {
	static UdpClientHandler handler = new CustomUdpClientHandler();
	static DeviceClient client = new DeviceClient();
	@BeforeClass
	public static void init() throws Exception {
		client.setup(handler);
	}
	@Test
	public void test() throws Exception {

		DeviceCommand cmd = new DeviceCommand();
		client.sendCommand("127.0.0.1", 123, cmd);
	}
}
