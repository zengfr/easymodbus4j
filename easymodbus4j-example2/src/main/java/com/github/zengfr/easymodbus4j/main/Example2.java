package com.github.zengfr.easymodbus4j.main;

import com.github.zengfr.easymodbus4j.app.ModbusServer4MasterApp;
/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
public class Example2 {
	public static void main(String[] args) throws Exception {
		if (args == null || args.length <= 0)
			args = new String[] { "" };
		String[] argsArray = args[0].split("[,;|]");
		switch (argsArray.length) {
		default:
			ModbusServer4MasterApp.init(argsArray);
			break;
		}
	}
}
