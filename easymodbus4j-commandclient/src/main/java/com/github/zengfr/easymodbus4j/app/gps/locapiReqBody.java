package com.github.zengfr.easymodbus4j.app.gps;
 
 
public class locapiReqBody {

   private String bts;
   private String output;
   private int accesstype;
   private String macs;
   private String imei;
   private String ctime;
   private String nearbts;
   private int cdma;
   private String need_rgc;
   private String network;
   public void setBts(String bts) {
        this.bts = bts;
    }
    public String getBts() {
        return bts;
    }

   public void setOutput(String output) {
        this.output = output;
    }
    public String getOutput() {
        return output;
    }

   public void setAccesstype(int accesstype) {
        this.accesstype = accesstype;
    }
    public int getAccesstype() {
        return accesstype;
    }

   public void setMacs(String macs) {
        this.macs = macs;
    }
    public String getMacs() {
        return macs;
    }

   public void setImei(String imei) {
        this.imei = imei;
    }
    public String getImei() {
        return imei;
    }

   public void setCtime(String ctime) {
        this.ctime = ctime;
    }
    public String getCtime() {
        return ctime;
    }

   public void setNearbts(String nearbts) {
        this.nearbts = nearbts;
    }
    public String getNearbts() {
        return nearbts;
    }

   public void setCdma(int cdma) {
        this.cdma = cdma;
    }
    public int getCdma() {
        return cdma;
    }

   public void setNeed_rgc(String need_rgc) {
        this.need_rgc = need_rgc;
    }
    public String getNeed_rgc() {
        return need_rgc;
    }

   public void setNetwork(String network) {
        this.network = network;
    }
    public String getNetwork() {
        return network;
    }

}