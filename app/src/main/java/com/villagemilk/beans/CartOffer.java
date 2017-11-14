package com.villagemilk.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akash.mercer on 23-02-2016.
 */
public class CartOffer {

    private String cartOfferString;

    private List<EntryItem> entryItemList = new ArrayList<>();

    private List<CartItem> cartItemList = new ArrayList<>();

    private List<NewSubscriptionMaster> subscriptionMasterList = new ArrayList<>();

    public ProductMaster getPopupProduct() {
        return popupProduct;
    }

    public void setPopupProduct(ProductMaster popupProduct) {
        this.popupProduct = popupProduct;
    }

    private ProductMaster popupProduct;

    public CartOffer(){

    }

    public String getCartOfferString() {
        return cartOfferString;
    }

    public void setCartOfferString(String cartOfferString) {
        this.cartOfferString = cartOfferString;
    }

    public List<EntryItem> getEntryItemList() {
        return entryItemList;
    }

    public void setEntryItemList(List<EntryItem> entryItemList) {
        this.entryItemList = entryItemList;
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public List<NewSubscriptionMaster> getSubscriptionMasterList() {
        return subscriptionMasterList;
    }

    public void setSubscriptionMasterList(List<NewSubscriptionMaster> subscriptionMasterList) {
        this.subscriptionMasterList = subscriptionMasterList;
    }
}
