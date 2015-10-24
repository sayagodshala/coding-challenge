package com.sayagodshala.dingdong.model;

import com.google.gson.annotations.SerializedName;

public class Meta {

    private boolean open;
    private String message;
    private LocationServed locationServed;
    private int startHour;
    private int endHour;
    @SerializedName("service_open")
    private String serviceOpen;

    public Meta(boolean open, String message, LocationServed locationServed, int startHour, int endHour) {
        this.open = open;
        this.message = message;
        this.locationServed = locationServed;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public Meta(boolean open, String message) {
        this.open = open;
        this.message = message;
    }

    public Meta(boolean open, String message, LocationServed locationServed) {
        this.open = open;
        this.message = message;
        this.locationServed = locationServed;
    }

    public Meta() {
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocationServed getLocationServed() {
        return locationServed;
    }

    public void setLocationServed(LocationServed locationServed) {
        this.locationServed = locationServed;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public String getServiceOpen() {
        return serviceOpen;
    }

    public void setServiceOpen(String serviceOpen) {
        this.serviceOpen = serviceOpen;
    }
}
