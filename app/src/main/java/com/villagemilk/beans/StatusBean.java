package com.villagemilk.beans;

/**
 * Created by akash.mercer on 25-02-2016.
 */
public class StatusBean {

    private int status;

    private String message;

    public StatusBean(){

    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
