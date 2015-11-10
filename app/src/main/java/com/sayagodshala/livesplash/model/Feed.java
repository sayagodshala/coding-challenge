package com.sayagodshala.livesplash.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Feed implements Parcelable {

    @SerializedName("entry")
    List<Entry> entry;

    public Feed() {
    }

    public List<Entry> getEntry() {
        return entry;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(entry);
    }

    protected Feed(Parcel in) {
        this.entry = in.createTypedArrayList(Entry.CREATOR);
    }

    public static final Parcelable.Creator<Feed> CREATOR = new Parcelable.Creator<Feed>() {
        public Feed createFromParcel(Parcel source) {
            return new Feed(source);
        }

        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };
}
