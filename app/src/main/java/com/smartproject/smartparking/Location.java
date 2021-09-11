package com.smartproject.smartparking;

public class Location {
    String Lati;
    String Longi;
    String Bikes;
    String Cars;
    String Park;
    String uniqueid;


    public Location(String lati, String longi, String bikes, String cars, String park, String uniqueid) {
        Lati = lati;
        Longi = longi;
        Bikes = bikes;
        Cars = cars;
        Park = park;
        this.uniqueid = uniqueid;
    }

    public String getLati() {
        return Lati;
    }

    public String getLongi() {
        return Longi;
    }

    public String getBikes() {
        return Bikes;
    }

    public String getCars() {
        return Cars;
    }

    public String getPark() {
        return Park;
    }

    public String getUniqueid() {
        return uniqueid;
    }
}
