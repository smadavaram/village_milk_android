package com.villagemilk.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akash.mercer on 29-Jul-16.
 */
public class Cart {

    private String userId;

    private List<ProductMaster> cartProductList = new ArrayList<>();

    private List<ProductMaster> comingTomorrowProductList = new ArrayList<>();

    private List<ProductMaster> totalProductList = new ArrayList<>();

    private ProductMaster offerProduct;

//    private int cartQuantity;
//
//    private int comingTomorrowProductQuantity;
//
//    private double cartAmount;
//
//    private double comingTomorrowProductAmount;

    private String promoCode;

    public Cart(){

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<ProductMaster> getCartProductList() {
        return cartProductList;
    }

    public void setCartProductList(List<ProductMaster> cartProductList) {
        this.cartProductList = cartProductList;
    }

    public List<ProductMaster> getComingTomorrowProductList() {
        return comingTomorrowProductList;
    }

    public void setComingTomorrowProductList(List<ProductMaster> comingTomorrowProductList) {
        this.comingTomorrowProductList = comingTomorrowProductList;
    }

    public List<ProductMaster> getTotalProductList() {
        return totalProductList;
    }

    public void setTotalProductList(List<ProductMaster> totalProductList) {
        this.totalProductList = totalProductList;
    }

    public ProductMaster getOfferProduct() {
        return offerProduct;
    }

    public void setOfferProduct(ProductMaster offerProduct) {
        this.offerProduct = offerProduct;
    }

//    public int getCartQuantity() {
//        return cartQuantity;
//    }
//
//    public void setCartQuantity(int cartQuantity) {
//        this.cartQuantity = cartQuantity;
//    }
//
//    public int getComingTomorrowProductQuantity() {
//        return comingTomorrowProductQuantity;
//    }
//
//    public void setComingTomorrowProductQuantity(int comingTomorrowProductQuantity) {
//        this.comingTomorrowProductQuantity = comingTomorrowProductQuantity;
//    }
//
//    public double getCartAmount() {
//        return cartAmount;
//    }
//
//    public void setCartAmount(double cartAmount) {
//        this.cartAmount = cartAmount;
//    }
//
//    public double getComingTomorrowProductAmount() {
//        return comingTomorrowProductAmount;
//    }
//
//    public void setComingTomorrowProductAmount(double comingTomorrowProductAmount) {
//        this.comingTomorrowProductAmount = comingTomorrowProductAmount;
//    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }
}
