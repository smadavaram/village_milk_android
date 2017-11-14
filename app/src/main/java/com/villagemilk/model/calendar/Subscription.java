
package com.villagemilk.model.calendar;


public class Subscription {

    private String id;
    private Long startDate;
    private Long endDate;
    private Integer subscriptionType;
    private Integer productQuantity;
    private Integer productTransientQuantity;
    private String productUnit;
    private Double productUnitCost;
    private Long createdAt;
    private String productImage;
    private Object offerId;
    private Integer productType;
    private String specialText;
    private Boolean rescheduledOrder;
    private Boolean cancelledByDN;
    private Integer userAction;
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDnAction() {
        return dnAction;
    }

    public void setDnAction(Integer dnAction) {
        this.dnAction = dnAction;
    }

    public Integer getUserAction() {
        return userAction;
    }

    public void setUserAction(Integer userAction) {
        this.userAction = userAction;
    }

    private Integer dnAction;
    private String productName;
    private String productMasterId;

    /**
     *
     * @return
     *     The productTransientQuantity
     */
    public Integer getProductTransientQuantity() {
        return productTransientQuantity;
    }

    /**
     *
     * @param productTransientQuantity
     *     The productTransientQuantity
     */
    public void setProductTransientQuantity(Integer productTransientQuantity) {
        this.productTransientQuantity = productTransientQuantity;
    }

    /**
     *
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     *     The startDate
     */
    public Long getStartDate() {
        return startDate;
    }

    /**
     *
     * @param startDate
     *     The startDate
     */
    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    /**
     * 
     * @return
     *     The endDate
     */
    public Long getEndDate() {
        return endDate;
    }

    /**
     * 
     * @param endDate
     *     The endDate
     */
    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    /**
     * 
     * @return
     *     The subscriptionType
     */
    public Integer getSubscriptionType() {
        return subscriptionType;
    }

    /**
     * 
     * @param subscriptionType
     *     The subscriptionType
     */
    public void setSubscriptionType(Integer subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    /**
     * 
     * @return
     *     The productQuantity
     */
    public Integer getProductQuantity() {
        return productQuantity;
    }

    /**
     * 
     * @param productQuantity
     *     The productQuantity
     */
    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    /**
     * 
     * @return
     *     The productMasterId
     */
    public String getProductMasterId() {
        return productMasterId;
    }

    /**
     * 
     * @param productMasterId
     *     The productMasterId
     */
    public void setProductMasterId(String productMasterId) {
        this.productMasterId = productMasterId;
    }

    /**
     * 
     * @return
     *     The productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 
     * @param productName
     *     The productName
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 
     * @return
     *     The productUnit
     */
    public String getProductUnit() {
        return productUnit;
    }

    /**
     * 
     * @param productUnit
     *     The productUnit
     */
    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    /**
     * 
     * @return
     *     The productUnitCost
     */
    public Double getProductUnitCost() {
        return productUnitCost;
    }

    /**
     * 
     * @param productUnitCost
     *     The productUnitCost
     */
    public void setProductUnitCost(Double productUnitCost) {
        this.productUnitCost = productUnitCost;
    }

    /**
     * 
     * @return
     *     The createdAt
     */
    public Long getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     * @param createdAt
     *     The createdAt
     */
    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 
     * @return
     *     The productImage
     */
    public String getProductImage() {
        return productImage;
    }

    /**
     * 
     * @param productImage
     *     The productImage
     */
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    /**
     * 
     * @return
     *     The offerId
     */
    public Object getOfferId() {
        return offerId;
    }

    /**
     * 
     * @param offerId
     *     The offerId
     */
    public void setOfferId(Object offerId) {
        this.offerId = offerId;
    }

    /**
     * 
     * @return
     *     The productType
     */
    public Integer getProductType() {
        return productType;
    }

    /**
     * 
     * @param productType
     *     The productType
     */
    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    /**
     * 
     * @return
     *     The specialText
     */
    public String getSpecialText() {
        return specialText;
    }

    /**
     * 
     * @param specialText
     *     The specialText
     */
    public void setSpecialText(String specialText) {
        this.specialText = specialText;
    }

    /**
     * 
     * @return
     *     The rescheduledOrder
     */
    public Boolean getRescheduledOrder() {
        return rescheduledOrder;
    }

    /**
     * 
     * @param rescheduledOrder
     *     The rescheduledOrder
     */
    public void setRescheduledOrder(Boolean rescheduledOrder) {
        this.rescheduledOrder = rescheduledOrder;
    }

    /**
     * 
     * @return
     *     The cancelledByDN
     */
    public Boolean getCancelledByDN() {
        return cancelledByDN;
    }

    /**
     * 
     * @param cancelledByDN
     *     The cancelledByDN
     */
    public void setCancelledByDN(Boolean cancelledByDN) {
        this.cancelledByDN = cancelledByDN;
    }

}
