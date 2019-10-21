package com.github.zengfr.easymodbus4j.app.gps;

import java.util.List;

public class locapiReq {
	 private String ver;
	    private boolean trace;
	    private String prod;
	    private String src;
	    private String key;
	    private List<locapiReqBody> body;
	    public void setVer(String ver) {
	         this.ver = ver;
	     }
	     public String getVer() {
	         return ver;
	     }

	    public void setTrace(boolean trace) {
	         this.trace = trace;
	     }
	     public boolean getTrace() {
	         return trace;
	     }

	    public void setProd(String prod) {
	         this.prod = prod;
	     }
	     public String getProd() {
	         return prod;
	     }

	    public void setSrc(String src) {
	         this.src = src;
	     }
	     public String getSrc() {
	         return src;
	     }

	    public void setKey(String key) {
	         this.key = key;
	     }
	     public String getKey() {
	         return key;
	     }

	    public void setBody(List<locapiReqBody> body) {
	         this.body = body;
	     }
	     public List<locapiReqBody> getBody() {
	         return body;
	     }
}
