package com.villagemilk.beans;

/**
 * Created by akash.mercer on 17-Sep-16.
 */

public class SubscriptionUpdateBean {

    private String subscriptionMasterId;

    private int quantity;

    private Long date;

    public SubscriptionUpdateBean(){

    }

    public String getSubscriptionMasterId() {
        return subscriptionMasterId;
    }

    public void setSubscriptionMasterId(String subscriptionMasterId) {
        this.subscriptionMasterId = subscriptionMasterId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
