package com.villagemilk.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by akash.mercer on 04-Sep-16.
 */

public class SubscriptionException {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("exceptionDate")
    @Expose
    private Long exceptionDate;
    @SerializedName("productQuantity")
    @Expose
    private Integer productQuantity;
    @SerializedName("subscriptionMaster")
    @Expose
    private String subscriptionMasterId;
    @SerializedName("subscriptionExceptionType")
    @Expose
    private Integer subscriptionExceptionType;

    public SubscriptionException(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getExceptionDate() {
        return exceptionDate;
    }

    public void setExceptionDate(Long exceptionDate) {
        this.exceptionDate = exceptionDate;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getSubscriptionMasterId() {
        return subscriptionMasterId;
    }

    public void setSubscriptionMasterId(String subscriptionMasterId) {
        this.subscriptionMasterId = subscriptionMasterId;
    }

    public Integer getSubscriptionExceptionType() {
        return subscriptionExceptionType;
    }

    public void setSubscriptionExceptionType(Integer subscriptionExceptionType) {
        this.subscriptionExceptionType = subscriptionExceptionType;
    }
}
