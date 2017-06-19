package com.memorynotfound.hibernate;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

    private String Street;
    private String city;
    private String zip;
    private String state;

    public Address() {
    }

    public Address(String street, String city, String zip, String state) {
        Street = street;
        this.city = city;
        this.zip = zip;
        this.state = state;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Address{" +
                "Street='" + Street + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
