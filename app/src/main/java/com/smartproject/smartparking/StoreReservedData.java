package com.smartproject.smartparking;

public class StoreReservedData {
    String Parking_name;
    String Name;
    String Mobile;
    String Vehical_no;
    String saveCurrentDate;
    String saveCurrentTime;
    String Vechical_Type;

    public StoreReservedData(String buttonselected,String parking_name, String name, String mobile, String vehicalno, String saveCurrentDate, String saveCurrentTime){

        this.Parking_name = parking_name;
        this.Name=name;
        this.Mobile=mobile;
        this.Vehical_no=vehicalno;
        this.saveCurrentDate = saveCurrentDate;
        this.saveCurrentTime = saveCurrentTime;
        this.Vechical_Type= buttonselected;

    }


    public String getParking_name() {
        return Parking_name;
    }

    public void setParking_name(String Parking_name) {
        this.Parking_name = Parking_name;
    }
    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }
    public String getVehical_no() {
        return Vehical_no;
    }

    public void setVehical_no(String Vehical_no) {
        this.Vehical_no = Vehical_no;
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
        return Vechical_Type;
    }

    public void setVechical_Type(String Vehical_Type) {
        this.Vechical_Type = Vehical_Type;
    }
}
