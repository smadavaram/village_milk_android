package com.villagemilk.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by akash.mercer on 04-Sep-16.
 */

public class SubscriptionMasterSmall implements Serializable{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("startDate")
    @Expose
    private Long startDate;
    @SerializedName("endDate")
    @Expose
    private Long endDate;
    @SerializedName("subscriptionType")
    @Expose
    private Integer subscriptionType;
    @SerializedName("productMasterId")
    @Expose
    private String productMasterId;
    @SerializedName("productQuantity")
    @Expose
    private Integer productQuantity;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("productUnit")
    @Expose
    private String productUnitSize;
    @SerializedName("productUnitCost")
    @Expose
    private Double productUnitCost;
    @SerializedName("productImage")
    @Expose
    private String productImageUrl;
    @SerializedName("productType")
    @Expose
    private Integer productType;
    @SerializedName("offerId")
    @Expose
    private String offerId;

    public ArrayList<String> getDeliveryDays() {
        return deliveryDays;
    }

    public void setDeliveryDays(ArrayList<String> deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    ArrayList<String> deliveryDays;

    public void addDeliveryDay(String day){

        if(deliveryDays == null)
            deliveryDays = new ArrayList<>();

        deliveryDays.add(day);

    }

    private String subscriptionExceptionId;

    private int transientCalendarProductQuantity;

    private int transientSubscriptionType;

    public SubscriptionMasterSmall(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public Integer getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(Integer subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public String getProductMasterId() {
        return productMasterId;
    }

    public void setProductMasterId(String productMasterId) {
        this.productMasterId = productMasterId;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductUnitSize() {
        return productUnitSize;
    }

    public void setProductUnitSize(String productUnitSize) {
        this.productUnitSize = productUnitSize;
    }

    public Double getProductUnitCost() {
        return productUnitCost;
    }

    public void setProductUnitCost(Double productUnitCost) {
        this.productUnitCost = productUnitCost;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getSubscriptionExceptionId() {
        return subscriptionExceptionId;
    }

    public void setSubscriptionExceptionId(String subscriptionExceptionId) {
        this.subscriptionExceptionId = subscriptionExceptionId;
    }

    public int getTransientCalendarProductQuantity() {
        return transientCalendarProductQuantity;
    }

    public void setTransientCalendarProductQuantity(int transientCalendarProductQuantity) {
        this.transientCalendarProductQuantity = transientCalendarProductQuantity;
    }

    public int getTransientSubscriptionType() {
        return transientSubscriptionType;
    }

    public void setTransientSubscriptionType(int transientSubscriptionType) {
        this.transientSubscriptionType = transientSubscriptionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubscriptionMasterSmall)) return false;

        SubscriptionMasterSmall that = (SubscriptionMasterSmall) o;

        return getId().equals(that.getId());

    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public SubscriptionMasterSmall clone() throws CloneNotSupportedException {
        return (SubscriptionMasterSmall) super.clone();
    }
}
