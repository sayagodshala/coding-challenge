package com.sayagodshala.livesplash.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {
    private String label;
    private Attribute attributes;

    public Image() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Attribute getAttributes() {
        return attributes;
    }

    public void setAttributes(Attribute attributes) {
        this.attributes = attributes;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.label);
        dest.writeParcelable(this.attributes, 0);
    }

    protected Image(Parcel in) {
        this.label = in.readString();
        this.attributes = in.readParcelable(Attribute.class.getClassLoader());
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
