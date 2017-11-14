package com.villagemilk.beans;

import java.io.Serializable;

/**
 * Created by kartheek on 19-05-2016.
 */
public class NewSubscriptionMaster  implements Serializable {

    private String id;

    private Long startDate;

    private Long endDate;

    private Integer productQuantity;

    private String productMasterId;

    private String productName;

    private String productUnit;

    private Double productUnitCost;

    private Long createdAt;

    private String productImage;

    private String offerId;

    public NewSubscriptionMaster(){

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

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }
}
