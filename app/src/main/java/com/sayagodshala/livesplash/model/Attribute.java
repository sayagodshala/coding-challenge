package com.sayagodshala.livesplash.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Attribute implements Parcelable {

    private String height;

    public Attribute(String height) {
        this.height = height;
    }


    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.height);
    }

    public Attribute() {
    }

    protected Attribute(Parcel in) {
        this.height = in.readString();
    }

    public static final Parcelable.Creator<Attribute> CREATOR = new Parcelable.Creator<Attribute>() {
        public Attribute createFromParcel(Parcel source) {
            return new Attribute(source);
        }

        public Attribute[] newArray(int size) {
            return new Attribute[size];
        }
    };
}
