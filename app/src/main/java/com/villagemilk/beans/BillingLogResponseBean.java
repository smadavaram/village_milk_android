package com.villagemilk.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akash.mercer on 16-Aug-16.
 */
public class BillingLogResponseBean {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("results")
    @Expose
    private List<BillingLog> billingLogList = new ArrayList<>();
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;

    public BillingLogResponseBean(){

    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BillingLog> getBillingLogList() {
        return billingLogList;
    }

    public void setBillingLogList(List<BillingLog> billingLogList) {
        this.billingLogList = billingLogList;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
