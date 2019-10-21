package com.github.zengfr.easymodbus4j.app.gps;
 
 
public class locapiRespBody {

	private int type;
    private String location;
    private int radius;
    private String country;
    private String province;
    private String city;
    private String citycode;
    private String district;
    private String road;
    private String ctime;
    private String indoor;
    private int error;
    public void setType(int type) {
         this.type = type;
     }
     public int getType() {
         return type;
     }

    public void setLocation(String location) {
         this.location = location;
     }
     public String getLocation() {
         return location;
     }

    public void setRadius(int radius) {
         this.radius = radius;
     }
     public int getRadius() {
         return radius;
     }

    public void setCountry(String country) {
         this.country = country;
     }
     public String getCountry() {
         return country;
     }

    public void setProvince(String province) {
         this.province = province;
     }
     public String getProvince() {
         return province;
     }

    public void setCity(String city) {
         this.city = city;
     }
     public String getCity() {
         return city;
     }

    public void setCitycode(String citycode) {
         this.citycode = citycode;
     }
     public String getCitycode() {
         return citycode;
     }

    public void setDistrict(String district) {
         this.district = district;
     }
     public String getDistrict() {
         return district;
     }

    public void setRoad(String road) {
         this.road = road;
     }
     public String getRoad() {
         return road;
     }

    public void setCtime(String ctime) {
         this.ctime = ctime;
     }
     public String getCtime() {
         return ctime;
     }

    public void setIndoor(String indoor) {
         this.indoor = indoor;
     }
     public String getIndoor() {
         return indoor;
     }

    public void setError(int error) {
         this.error = error;
     }
     public int getError() {
         return error;
     }


}