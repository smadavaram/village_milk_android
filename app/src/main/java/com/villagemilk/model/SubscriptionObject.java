
package com.villagemilk.model;


public class SubscriptionObject {


    private String id;  //yes
    private Long startDate;  //yes
    private Long endDate;  //yes
    private Integer subscriptionType;  //yes
    private Integer productQuantity;  //yes
    private String productMasterId;
    private String productName;  //yes
    private String productUnit;  //yes
    private Double productUnitCost;  //yes
    private Long createdAt;
    private String productImage;
    private Object offerId;
    private Integer productType;
    private String specialText;
    private Object userAction;
    private Object dnAction;
    private Integer subscriptionState;


    public Integer getSubscriptionState() {
        return subscriptionState;
    }

    public void setSubscriptionState(Integer subscriptionState) {
        this.subscriptionState = subscriptionState;
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

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductMasterId() {
        return productMasterId;
    }

    public void setProductMasterId(String productMasterId) {
        this.productMasterId = productMasterId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public Double getProductUnitCost() {
        return productUnitCost;
    }

    public void setProductUnitCost(Double productUnitCost) {
        this.productUnitCost = productUnitCost;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Object getOfferId() {
        return offerId;
    }

    public void setOfferId(Object offerId) {
        this.offerId = offerId;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public String getSpecialText() {
        return specialText;
    }

    public void setSpecialText(String specialText) {
        this.specialText = specialText;
    }

    public Object getUserAction() {
        return userAction;
    }

    public void setUserAction(Object userAction) {
        this.userAction = userAction;
    }

    public Object getDnAction() {
        return dnAction;
    }

    public void setDnAction(Object dnAction) {
        this.dnAction = dnAction;
    }

}
