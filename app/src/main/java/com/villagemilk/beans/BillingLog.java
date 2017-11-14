package com.villagemilk.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by akash.mercer on 16-Aug-16.
 */
public class BillingLog {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("properties")
    @Expose
    private BillingLogProperties billingLogProperties;

    public BillingLog(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BillingLogProperties getBillingLogProperties() {
        return billingLogProperties;
    }

    public void setBillingLogProperties(BillingLogProperties billingLogProperties) {
        this.billingLogProperties = billingLogProperties;
    }
}
