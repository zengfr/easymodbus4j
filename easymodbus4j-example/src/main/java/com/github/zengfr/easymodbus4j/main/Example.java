package com.github.zengfr.easymodbus4j.main;

import com.github.zengfr.easymodbus4j.example.ModbusConsoleApp;

/**
 * @author zengfr QQ:362505707/1163551688 Email:zengfr3000@qq.com
 *         https://github.com/zengfr/easymodbus4j
 */
public class Example {
	public static void main(String[] args) throws Exception {
		if (args == null || args.length <= 0)
			args = new String[] { "" };
		String[] argsArray = args[0].split("[,;|]");
		ModbusConsoleApp.initAndStart(argsArray);
	}
}
