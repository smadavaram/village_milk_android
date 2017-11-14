package com.villagemilk.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek_iyer on 09-03-2016.
 */
public class ProductSubCategory implements Serializable {

    private Integer productSubCategoryId;

    private String productSubCategoryImage;

    private String productSubCategoryName;

    private List<ProductMaster> productList = new ArrayList<>();

    private Integer weight;

    private String description;

    private Boolean _visible;

    public ProductSubCategory(){

    }

    public Integer getProductSubCategoryId() {
        return productSubCategoryId;
    }

    public void setProductSubCategoryId(Integer productSubCategoryId) {
        this.productSubCategoryId = productSubCategoryId;
    }

    public String getProductSubCategoryImage() {
        return productSubCategoryImage;
    }

    public void setProductSubCategoryImage(String productSubCategoryImage) {
        this.productSubCategoryImage = productSubCategoryImage;
    }

    public String getProductSubCategoryName() {
        return productSubCategoryName;
    }

    public void setProductSubCategoryName(String productSubCategoryName) {
        this.productSubCategoryName = productSubCategoryName;
    }

    public List<ProductMaster> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductMaster> productList) {
        this.productList = productList;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean get_visible() {
        return _visible;
    }

    public void set_visible(Boolean _visible) {
        this._visible = _visible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductSubCategory)) return false;

        ProductSubCategory that = (ProductSubCategory) o;

        return getProductSubCategoryId() != null ? getProductSubCategoryId().equals(that.getProductSubCategoryId()) : that.getProductSubCategoryId() == null;

    }

    @Override
    public int hashCode() {
        return getProductSubCategoryId() != null ? getProductSubCategoryId().hashCode() : 0;
    }
}
