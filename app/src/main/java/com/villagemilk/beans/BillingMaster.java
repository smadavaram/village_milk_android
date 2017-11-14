package com.villagemilk.beans;

import java.io.Serializable;

public class BillingMaster implements Serializable {

    private String id;

	private Double amount;

	private Double cashback;

	private Long endDate;

	private Long startDate;

	private Long paymentDate;

	private boolean current;

	private String bankTxnId;

	private String txnId;

	private User user;

	private PaymentType paymentType;

	private Integer transientPaymentType;

	private PayNinjaAgent payNinjaAgent;

	public BillingMaster() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getCashback() {
		return cashback;
	}

	public void setCashback(Double cashback) {
		this.cashback = cashback;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public Long getStartDate() {
		return startDate;
	}

	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}

	public Long getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Long paymentDate) {
		this.paymentDate = paymentDate;
	}

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	public String getBankTxnId() {
		return bankTxnId;
	}

	public void setBankTxnId(String bankTxnId) {
		this.bankTxnId = bankTxnId;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public Integer getTransientPaymentType() {
		return transientPaymentType;
	}

	public void setTransientPaymentType(Integer transientPaymentType) {
		this.transientPaymentType = transientPaymentType;
	}

	public PayNinjaAgent getPayNinjaAgent() {
		return payNinjaAgent;
	}

	public void setPayNinjaAgent(PayNinjaAgent payNinjaAgent) {
		this.payNinjaAgent = payNinjaAgent;
	}
}
