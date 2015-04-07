package io.rewyndr.reflectbig.model;

import java.io.Serializable;

/**
 * This is a model class which is used to store the location information of the user.
 * Created by Raja on 7/20/2014.
 */
public class AddressLocation implements Serializable{
    double longitude = 0;
    double latitude = 0;
    String address = "";
    int radiusFence = 0;
    String shortAddress = "";

    public String getShortAddress() {
        return shortAddress;
    }

    public void setShortAddress(String shortAddress) {
        this.shortAddress = shortAddress;
    }

    /**
     * Parameterised constructor
     * @param longitude
     * @param latitude
     * @param address
     */
    public AddressLocation(double latitude, double longitude, String address, int radiusFence) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.radiusFence = radiusFence;
    }

    public AddressLocation(double latitude, double longitude, int radiusFence) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.radiusFence = radiusFence;
    }

    /**
     * Getters and setters
     *
     */

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRadiusFence() {
        return radiusFence;
    }

    public void setRadiusFence(int radiusFence) {
        this.radiusFence = radiusFence;
    }

    @Override
    public String toString() {
        return this.address + " - Latitude : " + this.latitude + " Longitude : " + this.longitude + " - Short location : " + this.shortAddress;
    }
}
