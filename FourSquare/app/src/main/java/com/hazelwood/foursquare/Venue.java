package com.hazelwood.foursquare;

import java.io.Serializable;

/**
 * Created by Hazelwood on 8/18/15.
 */
public class Venue implements Serializable {
    public static final long serialVersionUID = 123456789L;

    String name, contactNumber, address;

    public Venue(String name_, String phoneNumber_, String addy_){
        this.name = name_;
        this.contactNumber= phoneNumber_;
        this.address = addy_;
    }

    public Venue(){

    }

    public String getAddress() {
        return address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getName() {
        return name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
