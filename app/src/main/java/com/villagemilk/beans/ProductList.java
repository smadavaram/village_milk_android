
package com.villagemilk.beans;


import java.io.Serializable;

public class ProductList  implements Serializable {

    private String id;
    private Object transientId;
    private String productName;
    private Integer productUnitPrice;
    private String productImage;
    private Integer productSubType;
    private String productUnitSize;
    private Boolean fulfilledByVendor;
    private String productAlias;
    private Integer status;
    private String specialText;
    private Object flashShortText;
    private Object flashStatus;
    private Integer strikePrice;
    private Object subscriptionOfferText;
    private Object weight;
    private Object transientProductType;
    private Object buildingName;
    private Object createdAt;
    private Object updatedAt;
    private ProductType productType;
    private Object building;
    private Object shop;
    private String poweredBy;
    private Object productCategory;
    private Object productSubCategory;
    private Object productParent;
    private Object featuredStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getTransientId() {
        return transientId;
    }

    public void setTransientId(Object transientId) {
        this.transientId = transientId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

//    @JsonProperty("productUnitPrice")
    public Integer getProductUnitPrice() {
        return productUnitPrice;
    }

//    @JsonProperty("productUnitPrice")
    public void setProductUnitPrice(Integer productUnitPrice) {
        this.productUnitPrice = productUnitPrice;
    }

//    @JsonProperty("productImage")
    public String getProductImage() {
        return productImage;
    }

//    @JsonProperty("productImage")
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

//    @JsonProperty("productSubType")
    public Integer getProductSubType() {
        return productSubType;
    }

//    @JsonProperty("productSubType")
    public void setProductSubType(Integer productSubType) {
        this.productSubType = productSubType;
    }

//    @JsonProperty("productUnitSize")
    public String getProductUnitSize() {
        return productUnitSize;
    }

//    @JsonProperty("productUnitSize")
    public void setProductUnitSize(String productUnitSize) {
        this.productUnitSize = productUnitSize;
    }

//    @JsonProperty("fulfilledByVendor")
    public Boolean getFulfilledByVendor() {
        return fulfilledByVendor;
    }

//    @JsonProperty("fulfilledByVendor")
    public void setFulfilledByVendor(Boolean fulfilledByVendor) {
        this.fulfilledByVendor = fulfilledByVendor;
    }

//    @JsonProperty("productAlias")
    public String getProductAlias() {
        return productAlias;
    }

//    @JsonProperty("productAlias")
    public void setProductAlias(String productAlias) {
        this.productAlias = productAlias;
    }

//    @JsonProperty("status")
    public Integer getStatus() {
        return status;
    }

//    @JsonProperty("status")
    public void setStatus(Integer status) {
        this.status = status;
    }

//    @JsonProperty("specialText")
    public String getSpecialText() {
        return specialText;
    }

//    @JsonProperty("specialText")
    public void setSpecialText(String specialText) {
        this.specialText = specialText;
    }

//    @JsonProperty("flashShortText")
    public Object getFlashShortText() {
        return flashShortText;
    }

//    @JsonProperty("flashShortText")
    public void setFlashShortText(Object flashShortText) {
        this.flashShortText = flashShortText;
    }

//    @JsonProperty("flashStatus")
    public Object getFlashStatus() {
        return flashStatus;
    }

//    @JsonProperty("flashStatus")
    public void setFlashStatus(Object flashStatus) {
        this.flashStatus = flashStatus;
    }

//    @JsonProperty("strikePrice")
    public Integer getStrikePrice() {
        return strikePrice;
    }

//    @JsonProperty("strikePrice")
    public void setStrikePrice(Integer strikePrice) {
        this.strikePrice = strikePrice;
    }

//    @JsonProperty("subscriptionOfferText")
    public Object getSubscriptionOfferText() {
        return subscriptionOfferText;
    }

//    @JsonProperty("subscriptionOfferText")
    public void setSubscriptionOfferText(Object subscriptionOfferText) {
        this.subscriptionOfferText = subscriptionOfferText;
    }

//    @JsonProperty("weight")
    public Object getWeight() {
        return weight;
    }

//    @JsonProperty("weight")
    public void setWeight(Object weight) {
        this.weight = weight;
    }

//    @JsonProperty("transientProductType")
    public Object getTransientProductType() {
        return transientProductType;
    }

//    @JsonProperty("transientProductType")
    public void setTransientProductType(Object transientProductType) {
        this.transientProductType = transientProductType;
    }

//    @JsonProperty("buildingName")
    public Object getBuildingName() {
        return buildingName;
    }

//    @JsonProperty("buildingName")
    public void setBuildingName(Object buildingName) {
        this.buildingName = buildingName;
    }

//    @JsonProperty("createdAt")
    public Object getCreatedAt() {
        return createdAt;
    }

//    @JsonProperty("createdAt")
    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

//    @JsonProperty("updatedAt")
    public Object getUpdatedAt() {
        return updatedAt;
    }

//    @JsonProperty("updatedAt")
    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

//    @JsonProperty("productType")
    public ProductType getProductType() {
        return productType;
    }

//    @JsonProperty("productType")
    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

//    @JsonProperty("building")
    public Object getBuilding() {
        return building;
    }

//    @JsonProperty("building")
    public void setBuilding(Object building) {
        this.building = building;
    }

//    @JsonProperty("shop")
    public Object getShop() {
        return shop;
    }

//    @JsonProperty("shop")
    public void setShop(Object shop) {
        this.shop = shop;
    }

//    @JsonProperty("poweredBy")
    public String getPoweredBy() {
        return poweredBy;
    }

//    @JsonProperty("poweredBy")
    public void setPoweredBy(String poweredBy) {
        this.poweredBy = poweredBy;
    }

//    @JsonProperty("productCategory")
    public Object getProductCategory() {
        return productCategory;
    }

//    @JsonProperty("productCategory")
    public void setProductCategory(Object productCategory) {
        this.productCategory = productCategory;
    }

//    @JsonProperty("productSubCategory")
    public Object getProductSubCategory() {
        return productSubCategory;
    }

//    @JsonProperty("productSubCategory")
    public void setProductSubCategory(Object productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

//    @JsonProperty("productParent")
    public Object getProductParent() {
        return productParent;
    }

//    @JsonProperty("productParent")
    public void setProductParent(Object productParent) {
        this.productParent = productParent;
    }

//    @JsonProperty("featuredStatus")
    public Object getFeaturedStatus() {
        return featuredStatus;
    }

//    @JsonProperty("featuredStatus")
    public void setFeaturedStatus(Object featuredStatus) {
        this.featuredStatus = featuredStatus;
    }

}
