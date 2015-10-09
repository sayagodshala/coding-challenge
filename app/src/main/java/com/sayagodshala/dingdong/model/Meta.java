package com.sayagodshala.dingdong.model;

public class Meta {

    private boolean open;
    private String message;
    private LocationServed locationServed;

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
}
