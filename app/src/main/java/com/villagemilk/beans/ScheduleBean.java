package com.villagemilk.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akash.mercer on 04-Sep-16.
 */

public class ScheduleBean {

    @SerializedName("subscriptionList")
    @Expose
    private List<SubscriptionMasterSmall> subscriptionMasterList = new ArrayList<>();

    @SerializedName("subscriptionExceptionList")
    @Expose
    private List<SubscriptionException> subscriptionExceptionList = new ArrayList<>();

    public ScheduleBean(){

    }

    public List<SubscriptionMasterSmall> getSubscriptionMasterList() {
        return subscriptionMasterList;
    }

    public void setSubscriptionMasterList(List<SubscriptionMasterSmall> subscriptionMasterList) {
        this.subscriptionMasterList = subscriptionMasterList;
    }

    public List<SubscriptionException> getSubscriptionExceptionList() {
        return subscriptionExceptionList;
    }

    public void setSubscriptionExceptionList(List<SubscriptionException> subscriptionExceptionList) {
        this.subscriptionExceptionList = subscriptionExceptionList;
    }
}
