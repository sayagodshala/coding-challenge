package com.sayagodshala.dingdong.network;

import com.sayagodshala.dingdong.model.Meta;

/**
 * Created by sayagodshala on 8/8/2015.
 */
public class APIResponse<T> {
    private boolean status;
    private String message;

    private boolean open;

    private T values;

    private Meta meta;

    public APIResponse() {
    }

    public T getValues() {
        return values;
    }

    public void setValues(T values) {
        this.values = values;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
