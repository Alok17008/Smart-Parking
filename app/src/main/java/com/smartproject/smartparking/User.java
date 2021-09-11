package com.smartproject.smartparking;

public class User {
    String Displayname;
    String Email;
    String Mobile1;
    String Gender;
    String as="Admin";

    public  User(String Username,String email,String mobile,String gender, String use){
        this.Displayname=Username;
        this.Email=email;
        this.Mobile1=mobile;
        this.Gender=gender;
        this.as=use;

    }

    public String getDisplayname() {
        return Displayname;
    }

    public String getEmail() {
        return Email;
    }

    public String getMobile(){
        return Mobile1;
    }

    public String getgender(){return Gender ;}
    public String getAs(){return as;}
}