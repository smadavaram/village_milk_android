package com.villagemilk.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akash.mercer on 23-02-2016.
 */
public class CartItemMaster {

    private String userId;

    private List<CartItem> cartItemList = new ArrayList<>();

    private String promoCode;

    public CartItemMaster(){

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }
}
