package com.villagemilk.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by akash.mercer on 21-Aug-16.
 */

public class BillingLogProperties {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("date")
    @Expose
    private Long date;
    @SerializedName("previous_balance")
    @Expose
    private Double previousBalance;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("daily_log_type")
    @Expose
    private Integer dailyLogType;
    @SerializedName("flat")
    @Expose
    private String flat;

    public BillingLogProperties(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Double getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(Double previousBalance) {
        this.previousBalance = previousBalance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDailyLogType() {
        return dailyLogType;
    }

    public void setDailyLogType(Integer dailyLogType) {
        this.dailyLogType = dailyLogType;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }
}
