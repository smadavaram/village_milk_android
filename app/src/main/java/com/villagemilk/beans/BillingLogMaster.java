package com.villagemilk.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by akash.mercer on 21-Aug-16.
 */

public class BillingLogMaster {

    private Long id;

    private Double balance;

    private List<BillingLog> billingLogList = new ArrayList<>();

    private Date date;

    private String dateString;

    private List<String> billingLogTransactionList = new ArrayList<>();

    public BillingLogMaster(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public List<BillingLog> getBillingLogList() {
        return billingLogList;
    }

    public void setBillingLogList(List<BillingLog> billingLogList) {
        this.billingLogList = billingLogList;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public List<String> getBillingLogTransactionList() {
        return billingLogTransactionList;
    }

    public void setBillingLogTransactionList(List<String> billingLogTransactionList) {
        this.billingLogTransactionList = billingLogTransactionList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BillingLogMaster)) return false;

        BillingLogMaster that = (BillingLogMaster) o;

        return getId() == that.getId();

    }

    @Override
    public int hashCode() {
        return getId().intValue();
    }
}
