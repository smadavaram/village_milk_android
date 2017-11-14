package com.villagemilk.beans;

/**
 * Created by akash.mercer on 15-12-2015.
 */
public class Payment {

    private String userId;

    private Double amount;

    private Integer paymentType;

    private String bankTxnId;

    public Integer getPaymentProvider() {
        return paymentProvider;
    }

    public void setPaymentProvider(Integer paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    private Integer paymentProvider;

    private String transactionId;

    private String payNinjaAgentUsername;

    private String payNinjaAgentPassword;

    private String orderId;

    public Payment(){

    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public String getBankTxnId() {
        return bankTxnId;
    }

    public void setBankTxnId(String bankTxnId) {
        this.bankTxnId = bankTxnId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPayNinjaAgentUsername() {
        return payNinjaAgentUsername;
    }

    public void setPayNinjaAgentUsername(String payNinjaAgentUsername) {
        this.payNinjaAgentUsername = payNinjaAgentUsername;
    }

    public String getPayNinjaAgentPassword() {
        return payNinjaAgentPassword;
    }

    public void setPayNinjaAgentPassword(String payNinjaAgentPassword) {
        this.payNinjaAgentPassword = payNinjaAgentPassword;
    }
}
