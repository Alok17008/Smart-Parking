package com.smartproject.smartparking;

public class Bookinguser {
    String parking_name;
    String name;
    String mobile;
    String vehical_no;
    String saveCurrentDate;
    String saveCurrentTime;
    String vechical_Type;

    public Bookinguser() {
    }

    public Bookinguser(String parking_name, String name, String mobile, String vehical_no, String saveCurrentDate, String saveCurrentTime, String vechical_Type) {
        this.parking_name = parking_name;
        this.name = name;
        this.mobile = mobile;
        this.vehical_no = vehical_no;
        this.saveCurrentDate = saveCurrentDate;
        this.saveCurrentTime = saveCurrentTime;
        this.vechical_Type = vechical_Type;
    }

    public String getParking_name() {
        return parking_name;
    }

    public void setParking_name(String parking_name) {
        this.parking_name = parking_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVehical_no() {
        return vehical_no;
    }

    public void setVehical_no(String vehical_no) {
        this.vehical_no = vehical_no;
    }

    public String getSaveCurrentDate() {
        return saveCurrentDate;
    }

    public void setSaveCurrentDate(String saveCurrentDate) {
        this.saveCurrentDate = saveCurrentDate;
    }

    public String getSaveCurrentTime() {
        return saveCurrentTime;
    }

    public void setSaveCurrentTime(String saveCurrentTime) {
        this.saveCurrentTime = saveCurrentTime;
    }

    public String getVechical_Type() {
        return vechical_Type;
    }

    public void setVechical_Type(String vechical_Type) {
        this.vechical_Type = vechical_Type;
    }
}
