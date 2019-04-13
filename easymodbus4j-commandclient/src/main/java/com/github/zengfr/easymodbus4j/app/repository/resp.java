package com.github.zengfr.easymodbus4j.app.repository;

import java.util.List;

public class resp<T> {
	public String msg;
	public Integer status;
	public List<T> results;
}
