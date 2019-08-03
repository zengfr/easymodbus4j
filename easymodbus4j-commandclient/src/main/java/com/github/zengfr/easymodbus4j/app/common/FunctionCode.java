/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.zengfr.easymodbus4j.app.common;

public class FunctionCode {

	public static final short READ_COILS = 0x01;
	public static final short READ_DISCRETE_INPUTS = 0x02;
	public static final short READ_HOLDING_REGISTERS = 0x03;
	public static final short READ_INPUT_REGISTERS = 0x04;
	public static final short WRITE_SINGLE_COIL = 0x05;
	public static final short WRITE_SINGLE_REGISTER = 0x06;
	public static final short READ_EXCEPTION_STATUS = 0x07;
	public static final short DIAGNOSTICS = 0x08;
	public static final short GET_COMM_EVENT_COUNTER = 0x0B;
	public static final short GET_COMM_EVENT_LOG = 0x0C;
	public static final short WRITE_MULTIPLE_COILS = 0x0F;
	public static final short WRITE_MULTIPLE_REGISTERS = 0x10;
	public static final short REPORT_SLAVEID = 0x11;
	public static final short READ_FILE_RECORD = 0x14;
	public static final short WRITE_FILE_RECORD = 0x15;
	public static final short MASK_WRITE_REGISTER = 0x16;
	public static final short READ_WRITE_MULTIPLE_REGISTERS = 0x17;
	public static final short READ_FIFO_QUEUE = 0x18;
	public static final short ENCAPSULATED_INTERFACE_TRANSPORT = 0x2B;
}