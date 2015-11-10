package com.sayagodshala.livesplash.notification;

public class DDNotification {

    public static final String TRANSACTIONAL_ORDER_PLACED = "TR-001";
    public static final String TRANSACTIONAL_ORDER_DISPATCHED = "TR-002";
    public static final String TRANSACTIONAL_ORDER_DELIVERED = "TR-003";
    public static final String UPDATE = "UP-001";

    private String status;
    private String type;
    private String message;
    private String title;
    private long time;

    public DDNotification(String status, String type, String message, String title, long time) {
        this.status = status;
        this.type = type;
        this.message = message;
        this.title = title;
        this.time = time;
    }

    public DDNotification() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    //    {"status":"dispatched","from":"309702514568","type":"transactional","message":"Your order has been dispatched","collapse_key":"do_not_collapse"}

}
