package com.villagemilk.model;

import com.villagemilk.beans.SubscriptionMaster;
import com.villagemilk.model.calendar.Subscription;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dailyninja on 11/08/17.
 */

public class SubscriptionWrapper implements Serializable{


    ArrayList<SubscriptionMaster> subscriptionMasters;
    String productMaster;

    ArrayList<String> subscriptionIds;

    public ArrayList<String> getSubscriptionIds() {
        return subscriptionIds;
    }

    public void setSubscriptionIds(ArrayList<String> subscriptionIds) {
        this.subscriptionIds = subscriptionIds;
    }

    public ArrayList<String> getDeliveryDays() {
        return deliveryDays;
    }

    public void setDeliveryDays(ArrayList<String> deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    ArrayList<String> deliveryDays;


    public SubscriptionWrapper() {

        subscriptionMasters = new ArrayList<>();
        deliveryDays = new ArrayList<>();
        subscriptionIds = new ArrayList<>();
    }

    public ArrayList<SubscriptionMaster> getSubscriptionMasters() {
        return subscriptionMasters;
    }

    public void setSubscriptionMasters(ArrayList<SubscriptionMaster> subscriptionMasters) {
        this.subscriptionMasters = subscriptionMasters;
    }

    public String getProductMaster() {
        return productMaster;
    }

    public void setProductMaster(String productMaster) {
        this.productMaster = productMaster;
    }
}
