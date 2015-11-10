package com.sayagodshala.livesplash.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Entry implements Parcelable {

    @SerializedName("im:name")
    private Name name;

    @SerializedName("im:image")
    private List<Image> image;

    private Summary summary;

    public Entry() {
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public List<Image> getImage() {
        return image;
    }

    public void setImage(List<Image> image) {
        this.image = image;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.name, 0);
        dest.writeTypedList(image);
        dest.writeParcelable(this.summary, 0);
    }

    protected Entry(Parcel in) {
        this.name = in.readParcelable(Name.class.getClassLoader());
        this.image = in.createTypedArrayList(Image.CREATOR);
        this.summary = in.readParcelable(Summary.class.getClassLoader());
    }

    public static final Creator<Entry> CREATOR = new Creator<Entry>() {
        public Entry createFromParcel(Parcel source) {
            return new Entry(source);
        }

        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };
}
