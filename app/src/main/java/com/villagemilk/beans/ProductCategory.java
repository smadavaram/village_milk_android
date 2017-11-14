package com.villagemilk.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek_iyer on 09-03-2016.
 */
public class ProductCategory implements Serializable {

    @SerializedName("productCategoryId")
    @Expose
    private Integer productCategoryId;
    @SerializedName("productCategoryImage")
    @Expose
    private String productCategoryImage;
    @SerializedName("productCategoryName")
    @Expose
    private String productCategoryName;
    @SerializedName("weight")
    @Expose
    private Integer weight;

    public List<ProductMaster> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductMaster> productList) {
        this.productList = productList;
    }

    @SerializedName("productList")
    @Expose
    private List<ProductMaster> productList = new ArrayList<>();
    @SerializedName("subCategories")
    @Expose
    private List<ProductSubCategory> subCategories = new ArrayList<>();
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("poweredBy")
    @Expose
    private String poweredBy;
    @SerializedName("bannerImage")
    @Expose
    private String bannerImage;
    @SerializedName("_visible")
    @Expose
    private Boolean visible;

    public ProductCategory(){

    }

    public Integer getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Integer productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getProductCategoryImage() {
        return productCategoryImage;
    }

    public void setProductCategoryImage(String productCategoryImage) {
        this.productCategoryImage = productCategoryImage;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public List<ProductSubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<ProductSubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPoweredBy() {
        return poweredBy;
    }

    public void setPoweredBy(String poweredBy) {
        this.poweredBy = poweredBy;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductCategory that = (ProductCategory) o;

        return getProductCategoryId().equals(that.getProductCategoryId());

    }

    @Override
    public String toString() {
        return getProductCategoryName();
    }
}
