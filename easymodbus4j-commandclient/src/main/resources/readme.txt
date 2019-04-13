quick start

client api 1:
DeviceClient.sendCommand(String host, int port, DeviceCommand<T> cmd);

client api 2(when functionCode:
WRITE_MULTIPLE_COILS = 0x0F;
WRITE_MULTIPLE_REGISTERS = 0x10;

DeviceClient.sendCommand2(String host, int port, DeviceCommand<T> cmd);

public class DeviceCommand<T> {
	/** 设备标识id(必选) */
	public String deviceId;
	/** 设备ip(可选) */
	public String ip;
	/** 设备port(可选) */
	public int port;
	/** 设备型号(可选) */
	public String version;
	/** 见class FunctionCode */
	public int functionCode;
	/** 寄存器地址 */
	public int address;
	/** 寄存器地址值 */
	public T value;
	/** 寄存器地址值 */
	public T[] values;
}