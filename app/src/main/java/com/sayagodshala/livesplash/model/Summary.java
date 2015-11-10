package com.sayagodshala.livesplash.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Summary implements Parcelable {

    private String label;

    public Summary() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.label);
    }

    protected Summary(Parcel in) {
        this.label = in.readString();
    }

    public static final Parcelable.Creator<Summary> CREATOR = new Parcelable.Creator<Summary>() {
        public Summary createFromParcel(Parcel source) {
            return new Summary(source);
        }

        public Summary[] newArray(int size) {
            return new Summary[size];
        }
    };
}
