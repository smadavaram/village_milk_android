package com.villagemilk.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.villagemilk.model.SubscriptionObject;
import com.villagemilk.model.calendar.Subscription;

import java.util.ArrayList;

/**
 * Created by akash.mercer on 15-Sep-16.
 */

public class SubscriptionMaster {


    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public void setOfferId(Object offerId) {
        this.offerId = offerId;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public void setSpecialText(String specialText) {
        this.specialText = specialText;
    }

    public void setUserAction(Object userAction) {
        this.userAction = userAction;
    }

    public void setDnAction(Object dnAction) {
        this.dnAction = dnAction;
    }

    public void setSubscriptionState(Integer subscriptionState) {
        this.subscriptionState = subscriptionState;
    }

    public void setProductMasterId(String productMasterId) {
        this.productMasterId = productMasterId;
    }

    private Long createdAt;
    private Object offerId;
    private Integer productType;
    private String specialText;
    private Object userAction;
    private Object dnAction;
    private Integer subscriptionState;
    private String productMasterId;

    public ArrayList<String> getDeliveryDays() {
        return deliveryDays;
    }

    public void setDeliveryDays(ArrayList<String> deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    ArrayList<String> deliveryDays;

    public ArrayList<SubscriptionMaster> getSubscriptionMasterArrayList() {
        return subscriptionMasterArrayList;
    }

    public void setSubscriptionMasterArrayList(ArrayList<SubscriptionMaster> subscriptionMasterArrayList) {
        this.subscriptionMasterArrayList = subscriptionMasterArrayList;
    }

    ArrayList<SubscriptionMaster> subscriptionMasterArrayList;

    public ArrayList<String> getSubscriptionIdList() {
        return subscriptionIdList;
    }

    public void setSubscriptionIdList(ArrayList<String> subscriptionIdList) {
        this.subscriptionIdList = subscriptionIdList;
    }

    ArrayList<String> subscriptionIdList;

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("startDate")
    @Expose
    private Long startDate;
    @SerializedName("endDate")
    @Expose
    private Long endDate;
    @SerializedName("verifyStatus")
    @Expose
    private Integer verifyStatus;
    @SerializedName("subscriptionType")
    @Expose
    private Integer subscriptionType;
    @SerializedName("productQuantity")
    @Expose
    private Integer productQuantity;
    @SerializedName("recentlyChangedFlag")
    @Expose
    private Boolean recentlyChangedFlag;
    @SerializedName("productMaster")
    @Expose
    private ProductMaster productMaster;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("productUnit")
    @Expose
    private String productUnit;
    @SerializedName("productUnitCost")
    @Expose
    private Double productUnitCost;
    @SerializedName("active")
    @Expose
    private Boolean active;

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    @SerializedName("productImage")
    @Expose
    private String productImage;

    private Integer transientSubscriptionType;

    public SubscriptionMaster(){

    }

    public void addDeliveryDay(String day){

        if(deliveryDays == null)
            deliveryDays = new ArrayList<>();

        deliveryDays.add(day);

    }


    public void addSubscriptionMaster(SubscriptionMaster subscriptionMaster){

        if(subscriptionMasterArrayList == null)
            subscriptionMasterArrayList = new ArrayList<>();

        subscriptionMasterArrayList.add(subscriptionMaster);

    }





    public void removeDeliveryDay(String day){

        if(deliveryDays == null)
            return;

        deliveryDays.remove(day);

    }


    public void addSubscriptionId(String id){

        if(subscriptionIdList == null)
            subscriptionIdList = new ArrayList<>();

        subscriptionIdList.add(id);


    }

    public SubscriptionMaster(SubscriptionMasterSmall subscriptionMasterSmall, String userId){
        this.id = subscriptionMasterSmall.getId();
        this.startDate = subscriptionMasterSmall.getStartDate();
        this.endDate = subscriptionMasterSmall.getEndDate();
        this.productName = subscriptionMasterSmall.getProductName();
        this.productQuantity = subscriptionMasterSmall.getProductQuantity();
        this.productUnit = subscriptionMasterSmall.getProductUnitSize();
        this.productUnitCost = subscriptionMasterSmall.getProductUnitCost();
        this.subscriptionType = subscriptionMasterSmall.getSubscriptionType();
        this.transientSubscriptionType = subscriptionMasterSmall.getSubscriptionType();
        this.setProductImage(subscriptionMasterSmall.getProductImageUrl());

        ProductMaster productMaster = new ProductMaster();
        productMaster.setId(subscriptionMasterSmall.getProductMasterId());
        this.productMaster = productMaster;

        User user = new User();
        user.setId(userId);
        this.user = user;

    }

    public void setUserId(String userId){

        User user = new User();
        user.setId(userId);
        this.user = user;

    }

    public SubscriptionMaster(SubscriptionObject subscriptionObject, String userId){

        this.id = subscriptionObject.getId();
        this.startDate = subscriptionObject.getStartDate();
        this.endDate = subscriptionObject.getEndDate();
        this.productName = subscriptionObject.getProductName();
        this.productQuantity = subscriptionObject.getProductQuantity();
        this.productUnit = subscriptionObject.getProductUnit();
        this.productUnitCost = subscriptionObject.getProductUnitCost();
        this.subscriptionType = subscriptionObject.getSubscriptionType();
        this.transientSubscriptionType = subscriptionObject.getSubscriptionType();
        setProductImage(subscriptionObject.getProductImage());
        this.startDate = subscriptionObject.getStartDate();
        this.endDate = subscriptionObject.getEndDate();
        this.offerId = subscriptionObject.getOfferId();
        this.productType = subscriptionObject.getProductType();
        this.specialText = subscriptionObject.getSpecialText();
        this.userAction = subscriptionObject.getUserAction();
        this.dnAction = subscriptionObject.getDnAction();
        this.subscriptionState = subscriptionObject.getSubscriptionState();
        this.productMasterId = subscriptionObject.getProductMasterId();

        ProductMaster productMaster = new ProductMaster();
        productMaster.setId(subscriptionObject.getProductMasterId());
        this.productMaster = productMaster;

        User user = new User();
        user.setId(userId);
        this.user = user;

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

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
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

    public Boolean getRecentlyChangedFlag() {
        return recentlyChangedFlag;
    }

    public void setRecentlyChangedFlag(Boolean recentlyChangedFlag) {
        this.recentlyChangedFlag = recentlyChangedFlag;
    }

    public ProductMaster getProductMaster() {
        return productMaster;
    }

    public void setProductMaster(ProductMaster productMaster) {
        this.productMaster = productMaster;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getTransientSubscriptionType() {
        return transientSubscriptionType;
    }

    public void setTransientSubscriptionType(Integer transientSubscriptionType) {
        this.transientSubscriptionType = transientSubscriptionType;
    }
}
