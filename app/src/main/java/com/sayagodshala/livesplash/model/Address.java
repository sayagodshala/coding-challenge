package com.sayagodshala.livesplash.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Address implements Parcelable {

    @SerializedName("address_id")
    private String addressId;
    private String address;
    private String landmark;
    private String latlng;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Address(String addressId, String address, String landmark, String latlng) {
        this.addressId = addressId;
        this.address = address;
        this.landmark = landmark;
        this.latlng = latlng;
    }

    public Address() {
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.addressId);
        dest.writeString(this.address);
        dest.writeString(this.landmark);
        dest.writeString(this.latlng);
    }

    protected Address(Parcel in) {
        this.addressId = in.readString();
        this.address = in.readString();
        this.landmark = in.readString();
        this.latlng = in.readString();
    }

    public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>() {
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
