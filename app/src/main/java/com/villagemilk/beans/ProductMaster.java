package com.villagemilk.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by akash.mercer on 11-03-2016.
 */
public class ProductMaster  implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("productUnitPrice")
    @Expose
    private Double productUnitPrice;
    @SerializedName("productImage")
    @Expose
    private String productImage;
    @SerializedName("productSubType")
    @Expose
    private Integer productSubType;
    @SerializedName("productUnitSize")
    @Expose
    private String productUnitSize;
    @SerializedName("fulfilledByVendor")
    @Expose
    private Boolean fulfilledByVendor;
    @SerializedName("productAlias")
    @Expose
    private String productAlias;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("specialText")
    @Expose
    private String specialText;
    @SerializedName("flashShortText")
    @Expose
    private String flashShortText;
    @SerializedName("flashStatus")
    @Expose
    private Integer flashStatus;
    @SerializedName("strikePrice")
    @Expose
    private Double strikePrice;
    @SerializedName("subscriptionOfferText")
    @Expose
    private String subscriptionOfferText;
    @SerializedName("weight")
    @Expose
    private Integer weight;
    @SerializedName("buildingName")
    @Expose
    private String buildingName;
    @SerializedName("poweredBy")
    @Expose
    private String poweredBy;
    @SerializedName("productCategory")
    @Expose
    private Integer productCategory;
    @SerializedName("productSubCategory")
    @Expose
    private Integer productSubCategory;
    @SerializedName("productParent")
    @Expose
    private Integer productParent;
    @SerializedName("buildingId")
    @Expose
    private String buildingId;
    @SerializedName("featuredStatus")
    @Expose
    private Integer featuredStatus;
    @SerializedName("productType")
    @Expose
    private ProductType productType;

    private int productQuantity;

    private int transientProductQuantity;

    private String subscriptionId;

    public ProductMaster(){

    }

    public ProductMaster(SubscriptionMasterSmall subscriptionMasterSmall){
        id = subscriptionMasterSmall.getProductMasterId();
        productImage = subscriptionMasterSmall.getProductImageUrl();
        productName = subscriptionMasterSmall.getProductName();
        productUnitSize = subscriptionMasterSmall.getProductUnitSize();
        productUnitPrice = subscriptionMasterSmall.getProductUnitCost();
        productQuantity = subscriptionMasterSmall.getProductQuantity();
        subscriptionId = subscriptionMasterSmall.getId();

        ProductType productType = new ProductType();
        productType.setType(subscriptionMasterSmall.getProductType());
        this.productType = productType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductUnitPrice() {
        return productUnitPrice;
    }

    public void setProductUnitPrice(Double productUnitPrice) {
        this.productUnitPrice = productUnitPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Integer getProductSubType() {
        return productSubType;
    }

    public void setProductSubType(Integer productSubType) {
        this.productSubType = productSubType;
    }

    public String getProductUnitSize() {
        return productUnitSize;
    }

    public void setProductUnitSize(String productUnitSize) {
        this.productUnitSize = productUnitSize;
    }

    public Boolean getFulfilledByVendor() {
        return fulfilledByVendor;
    }

    public void setFulfilledByVendor(Boolean fulfilledByVendor) {
        this.fulfilledByVendor = fulfilledByVendor;
    }

    public String getProductAlias() {
        return productAlias;
    }

    public void setProductAlias(String productAlias) {
        this.productAlias = productAlias;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSpecialText() {
        return specialText;
    }

    public void setSpecialText(String specialText) {
        this.specialText = specialText;
    }

    public String getFlashShortText() {
        return flashShortText;
    }

    public void setFlashShortText(String flashShortText) {
        this.flashShortText = flashShortText;
    }

    public Integer getFlashStatus() {
        return flashStatus;
    }

    public void setFlashStatus(Integer flashStatus) {
        this.flashStatus = flashStatus;
    }

    public Double getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(Double strikePrice) {
        this.strikePrice = strikePrice;
    }

    public String getSubscriptionOfferText() {
        return subscriptionOfferText;
    }

    public void setSubscriptionOfferText(String subscriptionOfferText) {
        this.subscriptionOfferText = subscriptionOfferText;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getPoweredBy() {
        return poweredBy;
    }

    public void setPoweredBy(String poweredBy) {
        this.poweredBy = poweredBy;
    }

    public Integer getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Integer productCategory) {
        this.productCategory = productCategory;
    }

    public Integer getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(Integer productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    public Integer getProductParent() {
        return productParent;
    }

    public void setProductParent(Integer productParent) {
        this.productParent = productParent;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public Integer getFeaturedStatus() {
        return featuredStatus;
    }

    public void setFeaturedStatus(Integer featuredStatus) {
        this.featuredStatus = featuredStatus;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getTransientProductQuantity() {
        return transientProductQuantity;
    }

    public void setTransientProductQuantity(int transientProductQuantity) {
        this.transientProductQuantity = transientProductQuantity;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductMaster)) return false;

        ProductMaster that = (ProductMaster) o;

        return getId().equals(that.getId());

    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
