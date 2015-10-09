package com.sayagodshala.dingdong.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {

    @SerializedName("order_id")
    private String orderId;
    private String status;
    private String paid;
    @SerializedName("created_date")
    private String createdDate;
    @SerializedName("order_key")
    private String orderKey;
    private Address address;
    private List<Product> products;

    public Order(String orderId, String status, String paid, String createdDate, String orderKey, Address address, List<Product> products) {
        this.orderId = orderId;
        this.status = status;
        this.paid = paid;
        this.createdDate = createdDate;
        this.orderKey = orderKey;
        this.address = address;
        this.products = products;
    }

    public Order() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
