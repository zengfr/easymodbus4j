package com.github.zengfr.easymodbus4j.app.repository;

import java.util.List;

public class update_modbus_valuesReq extends req {
	public String mainboard_no;
	public Long time_stamp;
	public List<update_modbus_valuesReqItem> items;
}
