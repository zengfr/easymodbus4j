package com.github.zengfr.easymodbus4j;

import java.util.List;

import org.junit.Test;

import com.github.zengfr.easymodbus4j.app.plugin.impl.DeviceCommandV1PluginImpl;
import com.github.zengfr.easymodbus4j.common.RegisterOrder;
import com.github.zengfr.easymodbus4j.common.util.ByteUtil;
import com.github.zengfr.easymodbus4j.common.util.HexUtil;
import com.github.zengfr.easymodbus4j.common.util.RegistersUtil;
import com.google.common.collect.Lists;

public class RegistersUtilTest {
	@Test
	public void testServer4MasterAndUdpServer() throws Exception {
		int[] vv = RegistersUtil.convertFloatToRegisters(6.5f, RegisterOrder.HighLow);
		System.out.print(vv);
		float ff = RegistersUtil.convertRegistersToFloat(vv, RegisterOrder.HighLow);
		System.out.print(ff);
		byte[] bytes1 = RegistersUtil.toByteArrayFloat(ff);
		byte[] bytes2 = ByteUtil.floatToByte(new float[] { ff });
		float[] ff2 = ByteUtil.toFloatArray(bytes2);
		System.out.print(ff2);
		int[] int2 = ByteUtil.toIntArray(bytes2);

		String ss = HexUtil.bytesToHexString(bytes2);
		System.out.print(ss);

		List<String> vArray = Lists.newArrayList();
		vArray.add("6.5");
		vArray.add("null");
		int[] s = DeviceCommandV1PluginImpl.covertToIntegerArray("float", vArray);
		System.out.print(s);
	}

	@Test
	public void testHexUtil() throws Exception {
		String hex = "DE0D";// 56845
		byte[] bytes = HexUtil.hexStringToByte(hex);

		short[] ss1 = ByteUtil.toShortArray(bytes, false);
		short[] ss2 = ByteUtil.toShortArray(bytes, true);
		System.out.println(ss1[0]);
		System.out.println(ss2[0]);
		System.out.println("" + ((short) ss1[0] & 0xffff));
		System.out.println("" + ((short) ss2[0] & 0xffff));

		System.out.println((0x10000 + ss2[0]));
		int[] r = ByteUtil.toUShortArray(bytes);
		System.out.println(r[0]);
	}
}
