package com.sayagodshala.livesplash.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Name implements Parcelable {

    private String label;

    public Name() {
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

    protected Name(Parcel in) {
        this.label = in.readString();
    }

    public static final Parcelable.Creator<Name> CREATOR = new Parcelable.Creator<Name>() {
        public Name createFromParcel(Parcel source) {
            return new Name(source);
        }

        public Name[] newArray(int size) {
            return new Name[size];
        }
    };
}
