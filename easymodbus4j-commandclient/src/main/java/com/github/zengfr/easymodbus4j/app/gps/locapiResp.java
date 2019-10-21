package com.github.zengfr.easymodbus4j.app.gps;

import java.util.List;

public class locapiResp {
	 private int errcode;
	    private String msg;
	    private List<locapiRespBody> body;
	    public void setErrcode(int errcode) {
	         this.errcode = errcode;
	     }
	     public int getErrcode() {
	         return errcode;
	     }

	    public void setMsg(String msg) {
	         this.msg = msg;
	     }
	     public String getMsg() {
	         return msg;
	     }

	    public void setBody(List<locapiRespBody> body) {
	         this.body = body;
	     }
	     public List<locapiRespBody> getBody() {
	         return body;
	     }
}
