package com.sayagodshala.dingdong.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sayagodshala on 9/20/2015.
 */
public class Customer implements Parcelable {

    @SerializedName("user_id")
    private String userId;

    private String name;
    private String email;

    @SerializedName("mobile_no")
    private String mobileNo;

    private String type;

    private String password;

    @SerializedName("first_order_discount")
    private String firstOrderDiscount;

    public Customer(String userId, String name, String email, String mobileNo, String type) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.mobileNo = mobileNo;
        this.type = type;
    }

    public Customer(String userId, String name, String email, String mobileNo, String type, String password, String firstOrderDiscount) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.mobileNo = mobileNo;
        this.type = type;
        this.password = password;
        this.firstOrderDiscount = firstOrderDiscount;
    }

    public Customer() {
    }

    public String getuserId() {
        return userId;
    }

    public void setuserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getmobileNo() {
        return mobileNo;
    }

    public void setmobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstOrderDiscount() {
        return firstOrderDiscount;
    }

    public void setFirstOrderDiscount(String firstOrderDiscount) {
        this.firstOrderDiscount = firstOrderDiscount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.mobileNo);
        dest.writeString(this.type);
        dest.writeString(this.password);
        dest.writeString(this.firstOrderDiscount);
    }

    protected Customer(Parcel in) {
        this.userId = in.readString();
        this.name = in.readString();
        this.email = in.readString();
        this.mobileNo = in.readString();
        this.type = in.readString();
        this.password = in.readString();
        this.firstOrderDiscount = in.readString();
    }

    public static final Parcelable.Creator<Customer> CREATOR = new Parcelable.Creator<Customer>() {
        public Customer createFromParcel(Parcel source) {
            return new Customer(source);
        }

        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };
}
